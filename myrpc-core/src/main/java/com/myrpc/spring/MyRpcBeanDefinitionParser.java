package com.myrpc.spring;

import com.myrpc.boot.config.ProtocolConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class MyRpcBeanDefinitionParser implements BeanDefinitionParser {

	protected Class<?> getBeanClass(Element element) {
		return null;
	}

	public final BeanDefinition parse(Element element, ParserContext parserContext) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();

		Class<?> beanClass = this.getBeanClass(element);
		if (beanClass != null) {
			builder.getRawBeanDefinition().setBeanClass(beanClass);
		}

		builder.getRawBeanDefinition().setSource(parserContext.extractSource(element));
		if (parserContext.isNested()) {
			builder.setScope(parserContext.getContainingBeanDefinition().getScope());
		}

		if (parserContext.isDefaultLazyInit()) {
			builder.setLazyInit(true);
		}

		this.doParse(element, parserContext, builder);

		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		String id = element.getAttribute("id");
		if ((id == null || id.length() == 0)) {
			String generatedBeanName = element.getAttribute("name");
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				if (ProtocolConfig.class.equals(getBeanClass(element))) {
					generatedBeanName = "myrpc";
				} else {
					generatedBeanName = element.getAttribute("interface");
				}
			}
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				generatedBeanName = getBeanClass(element).getName();
			}
			id = generatedBeanName;
			int counter = 2;
			while (parserContext.getRegistry().containsBeanDefinition(id)) {
				id = generatedBeanName + (counter++);
			}
		}
		if (id != null && id.length() > 0) {
			if (parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
			beanDefinition.getPropertyValues().addPropertyValue("id", id);
		}

		return beanDefinition;
	}

	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		this.doParse(element, builder);
	}

	protected void doParse(Element element, BeanDefinitionBuilder builder) {

	}
}
