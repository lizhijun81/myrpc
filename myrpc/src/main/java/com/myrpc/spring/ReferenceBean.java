package com.myrpc.spring;

import com.myrpc.config.ApplicationConfig;
import com.myrpc.config.ProtocolConfig;
import com.myrpc.config.ReferenceConfig;
import com.myrpc.config.RegistryConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class ReferenceBean<T> extends ReferenceConfig<T> implements FactoryBean, ApplicationContextAware, InitializingBean, DisposableBean {

	private static final long serialVersionUID = 4186914879813709242L;
	private transient ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() {
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
	}

	@Override
	public void destroy() throws Exception {

	}

	@Override
	public Object getObject() {
		return get();
	}

	@Override
	public Class<?> getObjectType() {
		return getInterfaceClass();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
