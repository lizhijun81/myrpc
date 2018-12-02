package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;

public interface RegistryService {

	void registerService(URL url);

	void unRegisterService(URL url);

	void registerConsumer(URL url);

	void unRegisterConsumer(URL url);

	void subscribeService(URL url, ServiceListener serviceListener);

	void unSubscribeService(URL url, ServiceListener serviceListener);

	List<URL> discoverService(URL url);
}