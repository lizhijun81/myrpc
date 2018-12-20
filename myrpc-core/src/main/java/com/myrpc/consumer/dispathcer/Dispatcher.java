package com.myrpc.consumer.dispathcer;

import com.myrpc.consumer.future.InvokeFuture;
import com.myrpc.model.RpcRequest;

public interface Dispatcher {

	<T> InvokeFuture dispatch(RpcRequest request, Class<T> returnType);
}
