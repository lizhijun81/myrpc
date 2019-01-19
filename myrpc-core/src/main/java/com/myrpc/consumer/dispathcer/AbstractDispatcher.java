package com.myrpc.consumer.dispathcer;


import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;
import com.myrpc.consumer.future.DefaultInvokeFuture;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.consumer.loadbalance.LoadBalance;
import com.myrpc.exception.MyRpcInvokeException;
import com.myrpc.exception.MyRpcRemotingException;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import com.myrpc.registry.Registry;
import com.myrpc.registry.RegistryListener;
import com.myrpc.rpc.Channel;
import com.myrpc.rpc.FutureListener;
import com.myrpc.transport.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.myrpc.common.StackTraceUtils.stackTrace;

abstract class AbstractDispatcher implements Dispatcher {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected static volatile Connector connector;
	protected final Map<String, Channel> channels = new ConcurrentHashMap<>();
	protected URL url;
	protected LoadBalance loadBalance;
	protected Registry registry;
	protected RegistryListener registryListener;

	protected AbstractDispatcher(URL url) {
		this.url = url;

		if (connector == null) {
			synchronized (AbstractDispatcher.class) {
				if (connector == null) {
					this.connector = ExtensionLoader.
							getExtension(Connector.class, url.getParameter(RpcConstants.TRANSPORTER_KEY)).with(url).init();
				}
			}
		}

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
		Set<String> newAllChannelSet = new HashSet<>();

		if (providerUrls != null) {
			if (providerUrls.size() > 0) {
				for (URL providerUrl : providerUrls) {
					String serverNodeAddress = providerUrl.getServerPortStr();
					if (!channels.keySet().contains(serverNodeAddress)) {

						providerUrl = providerUrl.addParameters(RpcConstants.CONNECTTIMEOUT_KEY, url.getParameter(RpcConstants.CONNECTTIMEOUT_KEY));

						Channel Channel = connector.connect(providerUrl);


						channels.put(serverNodeAddress, Channel);
					}
				}

				for (URL providerUrl : providerUrls) {
					newAllChannelSet.add(providerUrl.getServerPortStr());
				}
				for (Map.Entry entry : channels.entrySet()) {
					if (!newAllChannelSet.contains(entry.getKey())) {
						Channel channel = channels.get(entry.getKey());
						if (channel.isActive())
							channel.close();
						channels.remove(entry.getKey());
					}
				}
			} else {
				logger.error("No available server node. All server nodes are down !!!");
				for (Map.Entry entry : channels.entrySet()) {
					Channel channel = channels.get(entry.getKey());
					if (channel.isActive())
						channel.close();
					channels.remove(entry.getKey());
				}
				channels.clear();
			}
		}
		return new ArrayList<>(newAllChannelSet);
	}

	protected Channel getChannel(URL url) {

		List<String> serverNodes = getServerNodes(url);
		String serverNode = loadBalance.select(serverNodes);
		Channel channel = channels.get(serverNode);
		return channel;
	}

	protected <T> InvokeFuture<T> write(final RpcRequest request, final Class<T> returnType) {

		Channel channel = getChannel(url);

		final InvokeFuture<T> future = new DefaultInvokeFuture<T>(request).with(returnType);

		channel.write(request, new FutureListener<Channel>() {

			@Override
			public void operationSuccess(Channel channel) throws Exception {

			}

			@Override
			public void operationFailure(Channel channel, Throwable cause) throws Exception {
				if (logger.isWarnEnabled()) {
					logger.warn("Writes {} fail on {}, {}.", request, channel, stackTrace(cause));
				}

				RpcResponse response = new RpcResponse();
				response.setRequestId(request.getRequestId());
				response.setError(new MyRpcRemotingException(cause));
				DefaultInvokeFuture.fakeReceived(response);
			}
		});

		return future;
	}

}
