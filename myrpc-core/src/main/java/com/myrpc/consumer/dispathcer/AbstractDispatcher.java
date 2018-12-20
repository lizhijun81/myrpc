package com.myrpc.consumer.dispathcer;


import com.myrpc.common.RpcConstants;
import com.myrpc.boot.config.URL;
import com.myrpc.consumer.future.DefaultInvokeFuture;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.consumer.loadbalance.LoadBalance;
import com.myrpc.exception.MyRpcInvokeException;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.model.RpcRequest;
import com.myrpc.registry.Registry;
import com.myrpc.registry.RegistryListener;
import com.myrpc.transport.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractDispatcher implements Dispatcher {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected final Map<String, Connector> connectors = new ConcurrentHashMap<>();
	protected URL url;
	protected LoadBalance loadBalance;
	protected Registry registry;
	protected RegistryListener registryListener;

	protected AbstractDispatcher(URL url) {
		this.url = url;

		this.loadBalance = ExtensionLoader.getExtension(LoadBalance.class, url.getParameter(RpcConstants.LOADBALANCE_KEY));

		this.registry = ExtensionLoader.getExtension(Registry.class, url.getParameter(RpcConstants.REGISTRY_NAME_KEY)).with(url).init();

		this.registryListener = new RegistryListener(url);
		URL subscribeUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		this.registry.subscribe(subscribeUrl, registryListener);

		URL providerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.CONSUMERS_CATEGORY);
		this.registry.register(providerUrl);
	}

	protected List<String> getServerNodes(URL url) {
		List<URL> providerUrls = registryListener.getProviderUrls();
		if (providerUrls == null || providerUrls.isEmpty()) {
			throw new MyRpcInvokeException("providerUrl can not be empty");
		}
		Set<String> newAllServerNodeSet = new HashSet<>();

		if (providerUrls != null) {
			if (providerUrls.size() > 0) {
				for (URL providerUrl : providerUrls) {
					String serverNodeAddress = providerUrl.getServerPortStr();
					if (!connectors.keySet().contains(serverNodeAddress)) {

						providerUrl = providerUrl.addParameters(RpcConstants.CONNECTTIMEOUT_KEY, url.getParameter(RpcConstants.CONNECTTIMEOUT_KEY));

						Connector connector = ExtensionLoader.
								getExtension(Connector.class, url.getParameter(RpcConstants.TRANSPORTER_KEY)).with(providerUrl).init();

						if (!connector.isConnected())
							connector.connect();
						connectors.put(serverNodeAddress, connector);
					}
				}

				for (URL providerUrl : providerUrls) {
					newAllServerNodeSet.add(providerUrl.getServerPortStr());
				}
				for (Map.Entry entry : connectors.entrySet()) {
					if (!newAllServerNodeSet.contains(entry.getKey())) {
						Connector connector = connectors.get(entry.getKey());
						if (connector.isConnected())
							connector.disConnect();
						connectors.remove(entry.getKey());
					}
				}
			} else {
				logger.error("No available server node. All server nodes are down !!!");
				for (Map.Entry entry : connectors.entrySet()) {
					Connector connector = connectors.get(entry.getKey());
					if (connector.isConnected())
						connector.disConnect();
					connectors.remove(entry.getKey());
				}
				connectors.clear();
			}
		}
		return new ArrayList<>(newAllServerNodeSet);
	}

	protected Connector getConnector(URL url) {
		List<String> serverNodes = getServerNodes(url);
		String serverNode = loadBalance.select(serverNodes);
		Connector connector = connectors.get(serverNode);
		if (!connector.isConnected())
			connector.connect();
		return connector;
	}

	protected <T> InvokeFuture<T> write(final RpcRequest request, final Class<T> returnType) {
		final InvokeFuture<T> future = new DefaultInvokeFuture<T>(request).with(returnType);
		getConnector(url).request(request);
		return future;
	}

}
