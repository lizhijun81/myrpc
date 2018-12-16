package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

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