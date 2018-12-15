package com.frameworkrpc.consumer.cluster;

import com.frameworkrpc.consumer.future.InvokeFuture;
import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;

public interface ClusterInvoker {

	ClusterInvoker with(URL url);

	ClusterInvoker init();

	<T> InvokeFuture<T> invoke(RpcRequest request, Class<T> returnType);
}
