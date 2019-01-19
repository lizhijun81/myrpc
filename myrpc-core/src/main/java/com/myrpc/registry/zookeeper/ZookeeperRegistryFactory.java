package com.myrpc.registry.zookeeper;

import com.myrpc.config.URL;
import com.myrpc.extension.RpcComponent;
import com.myrpc.registry.Registry;
import com.myrpc.registry.RegistryFactory;

@RpcComponent(name = "zookeeper")
public class ZookeeperRegistryFactory implements RegistryFactory {

	@Override
	public Registry getRegistry(URL url) {
		return new ZookeeperRegistry(url);
	}
}
