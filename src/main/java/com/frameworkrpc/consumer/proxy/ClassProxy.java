package com.frameworkrpc.consumer.proxy;

import com.frameworkrpc.model.URL;

public interface ClassProxy {

	ClassProxy with(URL url);

	ClassProxy init();

	<T> T getInstance(final Class<T> inf);

}
