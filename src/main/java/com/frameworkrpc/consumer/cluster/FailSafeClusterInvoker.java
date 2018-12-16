package com.frameworkrpc.consumer.cluster;

import com.frameworkrpc.consumer.dispathcer.DefaultDispatcher;
import com.frameworkrpc.consumer.dispathcer.Dispatcher;
import com.frameworkrpc.consumer.future.FailSafeInvokeFuture;
import com.frameworkrpc.consumer.future.InvokeFuture;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;

@RpcComponent(name = "failsafe")
public class FailSafeClusterInvoker implements ClusterInvoker {

	private Dispatcher dispatcher;
	private URL url;

	@Override
	public ClusterInvoker with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public ClusterInvoker init() {
		this.dispatcher = new DefaultDispatcher(this.url);
		return this;
	}


	@Override
	public <T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType) {
		InvokeFuture<T> future = dispatcher.dispatch(request, returnType);
		return FailSafeInvokeFuture.with(future);
	}
}
