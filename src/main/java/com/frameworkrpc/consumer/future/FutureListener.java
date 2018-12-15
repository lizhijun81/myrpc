package com.frameworkrpc.consumer.future;

public interface FutureListener<V> {

	void complete(V result);


	void failure(Throwable cause);
}
