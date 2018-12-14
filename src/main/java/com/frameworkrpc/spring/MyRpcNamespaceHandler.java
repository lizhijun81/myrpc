package com.frameworkrpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyRpcNamespaceHandler extends NamespaceHandlerSupport {
	@Override
	public void init() {
		//registerBeanDefinitionParser("service", new MyRpcServiceParser());
		registerBeanDefinitionParser("registry", new MyRpcRegisteryParser());
		registerBeanDefinitionParser("application", new MyRpcApplicationParser());
		registerBeanDefinitionParser("protocol", new MyRpcProtocolParser());
		//registerBeanDefinitionParser("reference", new MyRpcReferenceParser());
	}
}
