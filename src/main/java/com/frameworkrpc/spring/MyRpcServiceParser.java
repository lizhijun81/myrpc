package com.frameworkrpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class MyRpcServiceParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return MyRpcServiceBean.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String interfaceName = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		String version = element.getAttribute("version");
		String group = element.getAttribute("group");
		String loadbalance = element.getAttribute("loadbalance");
		String timeout = element.getAttribute("timeout");
		String retries = element.getAttribute("retries");

		BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(ref);

		builder.addPropertyValue("interface", interfaceName);
		builder.addPropertyValue("ref", refBean);
		builder.addPropertyValue("version", version);
		builder.addPropertyValue("group", group);
		builder.addPropertyValue("loadbalance", loadbalance);
		builder.addPropertyValue("timeout", timeout);
		builder.addPropertyValue("retries", retries);
	}
}
