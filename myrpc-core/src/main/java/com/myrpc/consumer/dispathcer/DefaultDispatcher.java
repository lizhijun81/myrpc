package com.myrpc.consumer.dispathcer;

import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.model.RpcRequest;
import com.myrpc.boot.config.URL;

public class DefaultDispatcher extends AbstractDispatcher {

	public DefaultDispatcher(URL url) {
		super(url);
	}

	@Override
	public <T> InvokeFuture<T> dispatch(RpcRequest request, Class<T> returnType) {
		return write(request, returnType);
	}
}
