package com.frameworkrpc.demo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProviderWithSpring {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"myrpc-demo-provider.xml"});
		context.start();
		System.in.read();
	}
}
