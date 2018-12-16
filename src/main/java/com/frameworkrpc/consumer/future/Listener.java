package com.frameworkrpc.consumer.future;

public interface Listener<V> {

	void complete(V result);

	void failure(Throwable cause);
}
