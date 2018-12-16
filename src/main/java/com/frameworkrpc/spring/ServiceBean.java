package com.frameworkrpc.spring;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ProtocolConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.config.ServiceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

public class ServiceBean<T> extends ServiceConfig<T>
		implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware {

	private static final long serialVersionUID = 4186914879813709242L;
	private transient ApplicationContext applicationContext;
	private transient String beanName;

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (getApplication() == null) {
			Map<String, ApplicationConfig> applicationConfigMap = applicationContext == null ?
					null :
					BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
			if (applicationConfigMap != null && applicationConfigMap.size() > 0) {
				ApplicationConfig applicationConfig = null;
				for (ApplicationConfig config : applicationConfigMap.values()) {
					applicationConfig = config;
				}
				if (applicationConfig != null) {
					setApplication(applicationConfig);
				}
			}
		}

		if (getRegistry() == null) {
			Map<String, RegistryConfig> registryConfigMap = applicationContext == null ?
					null :
					BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
			if (registryConfigMap != null && registryConfigMap.size() > 0) {
				RegistryConfig registryConfig = null;
				for (RegistryConfig config : registryConfigMap.values()) {
					registryConfig = config;
				}
				if (registryConfig != null) {
					setRegistry(registryConfig);
				}
			}
		}
		if (getProtocol() == null) {
			Map<String, ProtocolConfig> protocolConfigMap = applicationContext == null ?
					null :
					BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
			if (protocolConfigMap != null && protocolConfigMap.size() > 0) {
				ProtocolConfig protocolConfig = null;
				for (ProtocolConfig config : protocolConfigMap.values()) {
					protocolConfig = config;
				}
				if (protocolConfig != null) {
					setProtocol(protocolConfig);
				}
			}
		}
		export();
	}

	@Override
	public void destroy() throws Exception {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

	}

}
