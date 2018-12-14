package com.frameworkrpc.spring;

import com.frameworkrpc.config.ServiceConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

public class MyRpcServiceParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ServiceConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {

	}
}
