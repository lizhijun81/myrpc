package com.frameworkrpc.proxy;

import com.frameworkrpc.model.URL;

public interface ObjectProxy {

	<T> T getInstance(final Class<T> inf, URL url);
}
