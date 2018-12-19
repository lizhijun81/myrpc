package com.myrpc.spring;

import com.myrpc.config.ServiceConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ServiceBean.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String interfaceName = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		String version = element.getAttribute("version");
		String group = element.getAttribute("group");
		String timeout = element.getAttribute("timeout");
//		String loadbalance = element.getAttribute("loadbalance");
//		String retries = element.getAttribute("retries");

		BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(ref);

		builder.addPropertyValue("interface", interfaceName);
		builder.addPropertyValue("ref", refBean);
		builder.addPropertyValue("version", version);
		builder.addPropertyValue("group", group);
		builder.addPropertyValue("timeout", timeout);
//		builder.addPropertyValue("loadbalance", loadbalance);
//		builder.addPropertyValue("retries", retries);
	}
}
