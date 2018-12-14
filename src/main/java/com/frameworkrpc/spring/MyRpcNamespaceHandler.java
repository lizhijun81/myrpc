package com.frameworkrpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyRpcNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("application", new MyRpcApplicationParser());
		registerBeanDefinitionParser("registry", new MyRpcRegisteryParser());
		registerBeanDefinitionParser("protocol", new MyRpcProtocolParser());
		registerBeanDefinitionParser("service", new MyRpcServiceParser());
		registerBeanDefinitionParser("reference", new MyRpcReferenceParser());
	}
}
