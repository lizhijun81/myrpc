package com.frameworkrpc.spring;

import com.frameworkrpc.config.RegistryConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class MyRpcRegisteryParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return RegistryConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");
		String name = element.getAttribute("name");
		String address = element.getAttribute("address");
		String timeout = element.getAttribute("timeout");
		String sessiontimeout = element.getAttribute("sessiontimeout");

		builder.setLazyInit(false);
		builder.addPropertyValue("name",name);
		builder.addPropertyValue("address",address);
		builder.addPropertyValue("timeout",timeout);
		builder.addPropertyValue("sessiontimeout",sessiontimeout);
	}
}
