package com.frameworkrpc.demo.provider;

import com.frameworkrpc.demo.api.DemoService;

public class DemoServiceImpl implements DemoService {
	@Override
	public String sayHello(String name) {
		return "Hello " + name;
	}
}
