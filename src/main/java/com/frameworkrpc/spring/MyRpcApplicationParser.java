package com.frameworkrpc.spring;

import com.frameworkrpc.config.ApplicationConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class MyRpcApplicationParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ApplicationConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String name = element.getAttribute("name");
		String version = element.getAttribute("version");

		builder.addPropertyValue("name", name);
		builder.addPropertyValue("version", version);
	}

}
