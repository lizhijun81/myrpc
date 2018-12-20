package com.myrpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class MyRpcNamespaceHandler extends NamespaceHandlerSupport {

//	static {
//		Resource resource = new ClassPathResource("logo.txt");
//		if (resource.exists()) {
//			try {
//				Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
//				String text = CharStreams.toString(reader);
//				System.out.println(text);
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	@Override
	public void init() {
		registerBeanDefinitionParser("application", new ApplicationParser());
		registerBeanDefinitionParser("registry", new RegisteryParser());
		registerBeanDefinitionParser("protocol", new ProtocolParser());
		registerBeanDefinitionParser("service", new ServiceParser());
		registerBeanDefinitionParser("reference", new ReferenceParser());
	}
}
