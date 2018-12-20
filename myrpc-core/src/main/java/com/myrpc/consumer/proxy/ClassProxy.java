package com.myrpc.consumer.proxy;

import com.myrpc.config.URL;

public interface ClassProxy {

	ClassProxy with(URL url);

	ClassProxy init();

	<T> T newInstance(final Class<T> inf);

}
