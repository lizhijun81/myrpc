package com.frameworkrpc.consumer.future;

import com.frameworkrpc.model.RpcResponse;

import java.util.concurrent.Future;

public interface InvokeFuture<V> extends Future<V> {

	InvokeFuture<V> with(Class<V> returnType);

	Class<V> returnType();

	void done(RpcResponse response);

	InvokeFuture<V> addListener(Listener<V> listener);

	InvokeFuture<V> addListeners(Listener<V>... listeners);

	InvokeFuture<V> removeListener(Listener<V> listener);

	InvokeFuture<V> removeListeners(Listener<V>... listeners);
}
