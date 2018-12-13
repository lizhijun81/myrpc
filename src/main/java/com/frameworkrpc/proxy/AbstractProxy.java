package com.frameworkrpc.proxy;

import com.frameworkrpc.client.Client;
import com.frameworkrpc.client.ClientFactory;
import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.exception.InvokeException;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.loadbalance.LoadBalance;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class AbstractProxy implements Serializable {

	private static final long serialVersionUID = 6592280954313884159L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected final Map<String, Client> serverClients = new ConcurrentHashMap<>();
	protected URL url;
	protected LoadBalance loadBalance;
	protected Registry registry;
	protected RegistryListener registryListener;

	protected AbstractProxy(URL url) {
		this.url = url;

		this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(url.getParameter(RpcConstants.LOADBALANCE_KEY));

		this.registry = ExtensionLoader.getExtensionLoader(RegistryFactory.class)
				.getExtension(url.getParameter(RpcConstants.REGISTRY_NAME_KEY), Scope.SINGLETON).getRegistry(url);

		this.registryListener = new RegistryListener(url);
		URL subscribeUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		this.registry.subscribe(subscribeUrl, registryListener);
		URL providerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.CONSUMERS_CATEGORY);
		this.registry.register(providerUrl);
	}

	protected List<String> getServerNodes(URL url) {
		List<URL> providerUrls = registryListener.getProviderUrls();
		if (providerUrls == null || providerUrls.isEmpty()) {
			throw new InvokeException("providerUrl can not be empty");
		}
		Set<String> newAllServerNodeSet = new HashSet<>();

		if (providerUrls != null) {
			if (providerUrls.size() > 0) {
				for (URL providerUrl : providerUrls) {
					String serverNodeAddress = providerUrl.getServerPortStr();
					if (!serverClients.keySet().contains(serverNodeAddress)) {
						providerUrl = providerUrl.addParameters(RpcConstants.CONNECTTIMEOUT_KEY, url.getParameter(RpcConstants.CONNECTTIMEOUT_KEY));
						Client client = ExtensionLoader.getExtensionLoader(ClientFactory.class)
								.getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON).getClient(providerUrl);
						if (!client.isConnected())
							client.doConnect();
						serverClients.put(serverNodeAddress, client);
					}
				}

				for (URL providerUrl : providerUrls) {
					newAllServerNodeSet.add(providerUrl.getServerPortStr());
				}
				for (Entry entry : serverClients.entrySet()) {
					if (!newAllServerNodeSet.contains(entry.getKey())) {
						Client client = serverClients.get(entry.getKey());
						if (client.isConnected())
							client.disConnect();
						serverClients.remove(entry.getKey());
					}
				}
			} else {
				logger.error("No available server node. All server nodes are down !!!");
				for (Entry entry : serverClients.entrySet()) {
					Client client = serverClients.get(entry.getKey());
					if (client.isConnected())
						client.disConnect();
					serverClients.remove(entry.getKey());
				}
				serverClients.clear();
			}
		}
		return new ArrayList<>(newAllServerNodeSet);
	}

	protected Object invocation(Object proxy, Method method, Object[] args) {
		RpcRequester request = new RpcRequester();
		request.setRequestId(UUID.randomUUID().toString());
		request.setInterfaceName(method.getDeclaringClass().getName());
		request.setMethodName(method.getName());
		request.setParameterTypes(method.getParameterTypes());
		request.setParameters(args);
		return getServerClient(url).request(request).get(url.getIntParameter(RpcConstants.TIMEOUT_KEY), TimeUnit.MILLISECONDS);
	}


	protected Client getServerClient(URL url) {
		List<String> serverNodes = getServerNodes(url);
		String serverNode = loadBalance.select(serverNodes);
		Client client = serverClients.get(serverNode);
		if (!client.isConnected())
			client.doConnect();
		return client;
	}

}
