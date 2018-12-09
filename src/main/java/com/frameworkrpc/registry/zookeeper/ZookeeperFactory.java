package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;

@RpcComponent(name = "zookeeper")
public class ZookeeperFactory implements RegistryFactory {

	volatile ZookeeperRegistry registry;

	@Override
	public Registry getRegistry(URL url) {
		if (registry == null) {
			synchronized (this) {
				if (registry == null) {
					registry = new ZookeeperRegistry(url, new ZkRegistryListener());
				}
			}
		}
		return registry;
	}
}
