package com.frameworkrpc.demo.provider;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.config.ServiceConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Provider {
	public static void main(String[] args) throws Exception {

		DemoService demoService = new DemoServiceImpl();

		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");

		RegistryConfig registry = new RegistryConfig();
		registry.setName("zookeeper");
		registry.setAddress("127.0.0.1:2181");

		ServiceConfig<DemoService> service = new ServiceConfig<DemoService>();
		service.setApplication(application);
		service.setRegistry(registry);
		service.setInterface(DemoService.class);
		service.setRef(demoService);
		service.setVersion("1.0.0");

		service.export();

		System.in.read();
	}
}
