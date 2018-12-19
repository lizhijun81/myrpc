package com.myrpc.consumer.proxy;

import com.myrpc.model.URL;

public interface ClassProxy {

	ClassProxy with(URL url);

	ClassProxy init();

	<T> T getInstance(final Class<T> inf);

}
