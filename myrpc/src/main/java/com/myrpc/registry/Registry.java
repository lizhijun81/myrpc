package com.myrpc.registry;

import com.myrpc.model.URL;

import java.util.Set;

public interface Registry {

	Registry with(URL url);

	Registry init();

	void register(URL url);

	void unregister(URL url);

	void subscribe(URL url, NotifyListener listener);

	void unsubscribe(URL url, NotifyListener listener);

	Set<URL> getRegisteredUrls();

	void close();

}