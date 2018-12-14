package com.frameworkrpc.spring;

import com.frameworkrpc.config.ProtocolConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

public class MyRpcProtocolParser extends MyRpcBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ProtocolConfig.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String name = element.getAttribute("name");
		String host = element.getAttribute("host");
		String port = element.getAttribute("port");
		String serialization = element.getAttribute("serialization");
		String transporter = element.getAttribute("transporter");
		String heartbeat = element.getAttribute("heartbeat");
		String threadpool = element.getAttribute("threadpool");
		String threads = element.getAttribute("threads");
		String iothreads = element.getAttribute("iothreads");

		builder.addPropertyValue("name",name);
		builder.addPropertyValue("host",host);
		builder.addPropertyValue("port",port);
		builder.addPropertyValue("serialization",serialization);
		builder.addPropertyValue("transporter",transporter);
		builder.addPropertyValue("heartbeat",heartbeat);
		builder.addPropertyValue("threadpool",threadpool);
		builder.addPropertyValue("threads",threads);
		builder.addPropertyValue("iothreads",iothreads);
	}
}
