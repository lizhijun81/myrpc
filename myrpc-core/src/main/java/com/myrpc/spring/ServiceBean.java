package com.myrpc.spring;

import com.myrpc.boot.config.ApplicationConfig;
import com.myrpc.boot.config.ProtocolConfig;
import com.myrpc.boot.config.RegistryConfig;
import com.myrpc.boot.config.ServiceConfig;
import com.myrpc.rpc.DefaultServer;
import com.myrpc.rpc.Server;
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
import java.util.concurrent.locks.ReentrantLock;

public class ServiceBean<T> extends ServiceConfig<T>
		implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener<ContextRefreshedEvent>, BeanNameAware {

	private static final long serialVersionUID = 4186914879813709242L;
	private transient ApplicationContext applicationContext;
	private transient String beanName;
	private static Server server;
	private static final ReentrantLock lock = new ReentrantLock();

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

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

		if (server == null) {
			try {
				lock.lock();
				if (server == null) {
					server = new DefaultServer().with(this).init();

					server.start();
				}

			} finally {
				lock.unlock();
			}
		}
		server.publish(this);
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
