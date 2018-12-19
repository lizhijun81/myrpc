package com.myrpc.consumer.cluster;

import com.myrpc.consumer.dispathcer.DefaultDispatcher;
import com.myrpc.consumer.dispathcer.Dispatcher;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.extension.RpcComponent;
import com.myrpc.model.RpcRequest;
import com.myrpc.config.URL;

@RpcComponent(name = "failfast")
public class FailFastClusterInvoker implements ClusterInvoker {

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
		return dispatcher.dispatch(request, returnType);
	}
}
