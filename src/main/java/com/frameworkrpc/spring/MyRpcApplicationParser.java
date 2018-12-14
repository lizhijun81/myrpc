package com.frameworkrpc.spring;

import com.frameworkrpc.common.StringUtils;
import com.frameworkrpc.config.ApplicationConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class MyRpcApplicationParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ApplicationConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String id = element.getAttribute("id");
		String name = element.getAttribute("name");
		String version = element.getAttribute("version");

		builder.addPropertyValue("id", StringUtils.isEmpty(id) ? name : id);
		builder.addPropertyValue("name", name);
		builder.addPropertyValue("version", version);
	}
}
