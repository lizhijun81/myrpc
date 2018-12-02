package com.frameworkrpc.registry;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.zookeeper.ZkServiceListener;
import com.frameworkrpc.registry.zookeeper.ZookeeperRegistry;

import java.util.concurrent.ConcurrentHashMap;

public class RegistryFactory {
	private final static ConcurrentHashMap<String, RegistryService> registryServiceMap = new ConcurrentHashMap<>();

	public static RegistryService createRegistry(URL url) {
		RegistryEnum registryEnum = RegistryEnum.getRegistryEnum(url.getParameter(RpcConstant.REGISTRYNAME));
		if (!registryServiceMap.containsKey(url.toFullStr())) {
			registryServiceMap.put(url.toFullStr(), createRegistry(url, registryEnum));
		}
		return registryServiceMap.get(url.toFullStr());
	}

	public static RegistryService createRegistry(URL url, RegistryEnum registryEnum) {
		switch (registryEnum) {
			case Zookeeper:
				return new ZookeeperRegistry(url, new ZkServiceListener());
			default:
				return new ZookeeperRegistry(url, new ZkServiceListener());
		}
	}
}
