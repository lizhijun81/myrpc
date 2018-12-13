package com.frameworkrpc.demo.consumer;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ProtocolConfig;
import com.frameworkrpc.config.ReferenceConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Consumer {
	public static void main(String[] args) throws Exception {
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setName("zookeeper");
		registry.setAddress("127.0.0.1:2181");

		// 服务提供者协议配置
		ProtocolConfig protocol = new ProtocolConfig();
		protocol.setSerialization("hessian");

		// 引用远程服务
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application);
		reference.setRegistry(registry);
		reference.setProtocol(protocol);
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");

		DemoService demoService = reference.get();
		while (true) {
			try {
				String hello = demoService.sayHello("world"); // call remote method
				System.out.println(hello); // get result
				Thread.sleep(1000);
			} catch (Throwable throwable) {
				throwable.printStackTrace();
			}
		}
	}
}
