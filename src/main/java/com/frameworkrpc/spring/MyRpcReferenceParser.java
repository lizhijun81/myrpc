package com.frameworkrpc.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class MyRpcReferenceParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return MyRpcReferenceBean.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String interfaceName = element.getAttribute("interface");
		String version = element.getAttribute("version");
		String group = element.getAttribute("group");
		String loadbalance = element.getAttribute("loadbalance");
		String timeout = element.getAttribute("timeout");
		String retries = element.getAttribute("retries");
		String connecttimeout = element.getAttribute("connecttimeout");

		builder.addPropertyValue("interface", interfaceName);
		builder.addPropertyValue("version", version);
		builder.addPropertyValue("group", group);
		builder.addPropertyValue("loadbalance", loadbalance);
		builder.addPropertyValue("timeout", timeout);
		builder.addPropertyValue("retries", retries);
		builder.addPropertyValue("connecttimeout", connecttimeout);
	}
}
