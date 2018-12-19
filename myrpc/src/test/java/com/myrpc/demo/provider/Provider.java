package com.myrpc.demo.provider;

import com.myrpc.config.ApplicationConfig;
import com.myrpc.config.RegistryConfig;
import com.myrpc.config.ServiceConfig;
import com.myrpc.demo.api.DemoService;
import com.myrpc.rpc.DefaultServer;
import com.myrpc.rpc.Server;

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

		Server server = new DefaultServer().with(service).init();

		server.publish(service);

		server.start();

		System.in.read();
	}
}
