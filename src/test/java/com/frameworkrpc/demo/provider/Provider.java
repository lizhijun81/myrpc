package com.frameworkrpc.demo.provider;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ProtocolConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.config.ServiceConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Provider {
	public static void main(String[] args) throws Exception {
		// 服务实现
		DemoService demoService = new DemoServiceImpl();

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

		// 服务提供者暴露服务配置
		ServiceConfig<DemoService> service = new ServiceConfig<DemoService>();
		service.setApplication(application);
		service.setRegistry(registry);
		service.setProtocol(protocol);
		service.setInterface(DemoService.class);
		service.setRef(demoService);
		service.setVersion("1.0.0");


		// 暴露及注册服务
		service.export();

		System.in.read();
	}
}
