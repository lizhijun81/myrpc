package com.myrpc.consumer.cluster;

import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.URL;

public interface ClusterInvoker {

	ClusterInvoker with(URL url);

	ClusterInvoker init();

	<T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType);
}
