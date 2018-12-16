package com.frameworkrpc.consumer.dispathcer;

import com.frameworkrpc.consumer.future.InvokeFuture;
import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;

public class DefaultDispatcher extends AbstractDispatcher {

	public DefaultDispatcher(URL url) {
		super(url);
	}

	@Override
	public <T> InvokeFuture<T> dispatch(RpcRequest request, Class<T> returnType) {
		return write(request, returnType);
	}
}
