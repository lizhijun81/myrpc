package com.frameworkrpc.demo.consumer;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ReferenceConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Consumer {
	public static void main(String[] args) throws Exception {

		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");

		RegistryConfig registry = new RegistryConfig();
		registry.setName("zookeeper");
		registry.setAddress("127.0.0.1:2181");

		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application);
		reference.setRegistry(registry);
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
