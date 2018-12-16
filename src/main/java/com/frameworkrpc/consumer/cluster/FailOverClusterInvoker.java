package com.frameworkrpc.consumer.cluster;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.consumer.dispathcer.DefaultDispatcher;
import com.frameworkrpc.consumer.dispathcer.Dispatcher;
import com.frameworkrpc.consumer.future.FailOverInvokeFuture;
import com.frameworkrpc.consumer.future.Listener;
import com.frameworkrpc.consumer.future.InvokeFuture;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.frameworkrpc.common.StackTraceUtils.stackTrace;

@RpcComponent(name = "failover")
public class FailOverClusterInvoker implements ClusterInvoker {

	private static final Logger logger = LoggerFactory.getLogger(ClusterInvoker.class);
	private Dispatcher dispatcher;
	private URL url;
	private int retries;

	@Override
	public ClusterInvoker with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public ClusterInvoker init() {
		this.dispatcher = new DefaultDispatcher(this.url);
		this.retries = url.getIntParameter(RpcConstants.RETRIES_KEY);
		if (this.retries < 0) {
			this.retries = 2;
		}
		return this;
	}


	@Override
	public <T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType) {

		FailOverInvokeFuture<T> future = new FailOverInvokeFuture().with(returnType);

		int tryCount = retries + 1;
		invoke0(request, returnType, tryCount, future, null);

		return future;
	}

	private <T> void invoke0(final RpcRequest request, final Class<T> returnType, final int tryCount, final FailOverInvokeFuture<T> failOverFuture,
			Throwable lastCause) {

		if (tryCount > 0) {
			final InvokeFuture<T> future = dispatcher.dispatch(request, returnType);

			future.addListener(new Listener<T>() {

				@Override
				public void complete(T result) {
					failOverFuture.setSuccess(result);
				}

				@Override
				public void failure(Throwable cause) {
					if (logger.isWarnEnabled()) {

						logger.warn("[Fail-over] retry, [{}] attempts left, [method: {}], {}.", tryCount - 1, request.getMethodName(),
								stackTrace(cause));
					}

					invoke0(request, returnType, tryCount - 1, failOverFuture, cause);
				}
			});
		} else {
			failOverFuture.setFailure(lastCause);
		}
	}
}
