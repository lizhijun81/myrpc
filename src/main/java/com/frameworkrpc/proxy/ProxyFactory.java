package com.frameworkrpc.proxy;

import com.frameworkrpc.model.URL;

public interface ProxyFactory {

	<T> T getInstance(final Class<T> inf, URL url);

	void initProxy(URL url);
}
