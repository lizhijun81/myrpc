package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;

public interface Registry {

	void registerService(URL url);

	void unRegisterService(URL url);

	void registerConsumer(URL url);

	void unRegisterConsumer(URL url);

	void subscribeService(URL url);

	void unSubscribeService(URL url);

	List<URL> discoverService(URL url);
}