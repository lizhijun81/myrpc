package com.frameworkrpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyRpcNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("application", new ApplicationParser());
		registerBeanDefinitionParser("registry", new RegisteryParser());
		registerBeanDefinitionParser("protocol", new ProtocolParser());
		registerBeanDefinitionParser("service", new ServiceParser());
		registerBeanDefinitionParser("reference", new ReferenceParser());
	}
}
