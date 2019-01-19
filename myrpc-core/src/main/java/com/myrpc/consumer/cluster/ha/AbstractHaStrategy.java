package com.myrpc.consumer.cluster.ha;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;
import com.myrpc.consumer.cluster.HaStrategy;
import com.myrpc.consumer.cluster.LoadBalance;
import com.myrpc.consumer.future.DefaultInvokeFuture;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.consumer.proxy.AbstractProxy;
import com.myrpc.exception.MyRpcRemotingException;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import com.myrpc.rpc.Channel;
import com.myrpc.rpc.FutureListener;
import com.myrpc.transport.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.myrpc.common.StackTraceUtils.stackTrace;

abstract class AbstractHaStrategy implements HaStrategy {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected URL url;
	protected static volatile Connector connector;
	protected LoadBalance loadBalance;
	protected final Map<String, Channel> channels = new ConcurrentHashMap<>();

	@Override
	public HaStrategy with(URL url, LoadBalance loadBalance) {
		this.url = url;
		this.loadBalance = loadBalance;
		return this;
	}


	public HaStrategy init() {

		if (connector == null) {
			synchronized (AbstractProxy.class) {
				if (connector == null) {
					this.connector = ExtensionLoader.
							getExtension(Connector.class, url.getParameter(RpcConstants.TRANSPORTER_KEY)).with(url).init();
				}
			}
		}

		return this;
	}

	protected Channel getChannel() {

		URL providerUrl = loadBalance.getProviderUrl();
		providerUrl = providerUrl.addParameters(RpcConstants.CONNECTTIMEOUT_KEY, url.getParameter(RpcConstants.CONNECTTIMEOUT_KEY));

		if (!channels.containsKey(providerUrl.getServerPortStr())) {
			synchronized (this) {
				if (!channels.containsKey(providerUrl.getServerPortStr())) {
					Channel newChannel = connector.connect(providerUrl);
					channels.put(providerUrl.getServerPortStr(), newChannel);
				}
			}
		}

		return channels.get(providerUrl.getServerPortStr());
	}

	protected <T> InvokeFuture<T> dispatch(final Channel channel, final RpcRequest request, final Class<T> returnType) {

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
