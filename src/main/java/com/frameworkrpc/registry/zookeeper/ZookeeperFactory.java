package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.annotation.RpcComponent;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;

@RpcComponent(name = "zookeeper")
public class ZookeeperFactory implements RegistryFactory {

	@Override
	public Registry getRegistry(URL url) {
		return new ZookeeperRegistry(url, new ZkServiceListener());
	}
}
