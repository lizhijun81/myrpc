package com.myrpc.spring;

import com.myrpc.boot.config.ApplicationConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class ApplicationParser extends MyRpcBeanDefinitionParser {

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
