package com.myrpc.consumer.cluster.ha;

import com.myrpc.consumer.cluster.HaStrategy;
import com.myrpc.consumer.future.FailSafeInvokeFuture;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.extension.RpcComponent;
import com.myrpc.model.RpcRequest;

@RpcComponent(name = "failsafe")
public class FailSafeHaStrategy extends AbstractHaStrategy {

	@Override
	public HaStrategy init() {
		return super.init();
	}

	@Override
	public <T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType) {

		InvokeFuture<T> future = dispatch(getChannel(), request, returnType);
		return FailSafeInvokeFuture.with(future);
	}
}
