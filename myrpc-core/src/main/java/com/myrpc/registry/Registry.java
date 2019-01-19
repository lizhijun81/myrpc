package com.myrpc.registry;

import com.myrpc.config.URL;

import java.util.Set;

public interface Registry {

	void register(URL url);

	void unregister(URL url);

	void subscribe(URL url, NotifyListener listener);

	void unsubscribe(URL url, NotifyListener listener);

	Set<URL> getRegisteredUrls();

	void close();

}