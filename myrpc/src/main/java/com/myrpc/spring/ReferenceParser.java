package com.myrpc.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class ReferenceParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ReferenceBean.class;
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
		String proxy = element.getAttribute("proxy");
		String cluster = element.getAttribute("cluster");

		builder.addPropertyValue("interface", interfaceName);
		builder.addPropertyValue("version", version);
		builder.addPropertyValue("group", group);
		builder.addPropertyValue("loadbalance", loadbalance);
		builder.addPropertyValue("timeout", timeout);
		builder.addPropertyValue("retries", retries);
		builder.addPropertyValue("connecttimeout", connecttimeout);
		builder.addPropertyValue("proxy", proxy);
		builder.addPropertyValue("cluster", cluster);
	}
}
