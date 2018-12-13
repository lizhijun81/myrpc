package com.frameworkrpc.proxy;

public interface ClassProxy {

	<T> T getInstance(final Class<T> inf);

}
