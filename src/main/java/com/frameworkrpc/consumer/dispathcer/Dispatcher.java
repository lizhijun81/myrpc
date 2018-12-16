package com.frameworkrpc.consumer.dispathcer;

import com.frameworkrpc.consumer.future.InvokeFuture;
import com.frameworkrpc.model.RpcRequest;

public interface Dispatcher {

	<T> InvokeFuture dispatch(RpcRequest request, Class<T> returnType);
}
