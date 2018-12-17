package com.frameworkrpc.spring;

import com.google.common.io.CharStreams;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class MyRpcNamespaceHandler extends NamespaceHandlerSupport {

	static {
		Resource resource = new ClassPathResource("logo.txt");
		if (resource.exists()) {
			try {
				Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
				String text = CharStreams.toString(reader);
				System.out.println(text);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void init() {
		registerBeanDefinitionParser("application", new ApplicationParser());
		registerBeanDefinitionParser("registry", new RegisteryParser());
		registerBeanDefinitionParser("protocol", new ProtocolParser());
		registerBeanDefinitionParser("service", new ServiceParser());
		registerBeanDefinitionParser("reference", new ReferenceParser());
	}
}
