package com.frameworkrpc.spring;

import com.frameworkrpc.rpc.config.RegistryConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class MyRpcRegisteryParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return RegistryConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String name = element.getAttribute("name");
		String address = element.getAttribute("address");
		String timeout = element.getAttribute("timeout");
		String sessiontimeout = element.getAttribute("sessiontimeout");

		builder.addPropertyValue("name",name);
		builder.addPropertyValue("address",address);
		builder.addPropertyValue("timeout",timeout);
		builder.addPropertyValue("sessiontimeout",sessiontimeout);
	}
}
