package com.frameworkrpc.consumer.future;

import java.util.concurrent.Future;

public interface ListenableFuture<V> extends Future<V> {

	ListenableFuture<V> addListener(FutureListener<V> listener);

	ListenableFuture<V> addListeners(FutureListener<V>... listeners);

	ListenableFuture<V> removeListener(FutureListener<V> listener);

	ListenableFuture<V> removeListeners(FutureListener<V>... listeners);
}
