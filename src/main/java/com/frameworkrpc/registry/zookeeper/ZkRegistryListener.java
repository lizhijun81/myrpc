package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryListener;

import java.util.ArrayList;
import java.util.List;

public class ZkRegistryListener implements RegistryListener {

	@Override
	public void notifyRegistryUrl(List<URL> registryUrls, List<URL> urls) {
		if (registryUrls.size() <= 0) {
			urls = new ArrayList<>();
			return;
		}
		urls = registryUrls;
	}
}
