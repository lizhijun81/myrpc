package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;

public interface RegistryListener {

	void notifyRegistryUrl(List<URL> registryUrls, List<URL> urls);

}
