package com.myrpc.consumer.cluster;

import com.myrpc.config.URL;
import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.model.RpcRequest;

public interface HaStrategy {

	HaStrategy with(URL url, LoadBalance loadBalance);

	HaStrategy init();

	<T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType);
}
