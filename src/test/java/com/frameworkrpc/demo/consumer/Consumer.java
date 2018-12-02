package com.frameworkrpc.demo.consumer;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ReferenceConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Consumer {
	public static void main(String[] args) throws Exception {
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("yyy");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setRegistryname("zookeeper");
		registry.setAddress("127.0.0.1:2181");

		// 引用远程服务
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application);
		reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");
	}
}
