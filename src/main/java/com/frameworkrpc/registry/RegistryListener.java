package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface RegistryListener {

	void notifyRegistryUrl(List<URL> registryUrls, ConcurrentHashMap<String, List<URL>> urls);

}
