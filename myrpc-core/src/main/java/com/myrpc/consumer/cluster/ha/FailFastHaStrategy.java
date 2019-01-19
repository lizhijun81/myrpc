package com.myrpc.consumer.cluster.ha;

import com.myrpc.consumer.cluster.HaStrategy;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.extension.RpcComponent;
import com.myrpc.model.RpcRequest;

@RpcComponent(name = "failfast")
public class FailFastHaStrategy extends AbstractHaStrategy {

	@Override
	public HaStrategy init() {
		return super.init();
	}

	@Override
	public <T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType) {
		return dispatch(getChannel(), request, returnType);
	}
}
