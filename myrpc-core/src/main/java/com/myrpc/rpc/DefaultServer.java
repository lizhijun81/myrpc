package com.myrpc.rpc;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.ServiceConfig;
import com.myrpc.config.URL;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.extension.Scope;
import com.myrpc.registry.Registry;
import com.myrpc.registry.RegistryFactory;
import com.myrpc.transport.Acceptor;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class DefaultServer implements Server {

	protected final Set<ServiceConfig> currentServiceConfigs = newConcurrentHashSet();
	private Acceptor acceptor;
	private Registry registry;
	private InstanceFactory instanceFactory;
	private URL url;

	@Override
	public URL url() {
		return url;
	}

	@Override
	public Server with(ServiceConfig serviceConfig) {
		this.url = serviceConfig.url();
		return this;
	}

	@Override
	public Server init() {

		this.acceptor = ExtensionLoader.getExtension(Acceptor.class, url.getParameter(RpcConstants.TRANSPORTER_KEY)).with(url).init();

		this.registry = ExtensionLoader.getExtension(RegistryFactory.class, url.getParameter(RpcConstants.REGISTRY_NAME_KEY)).getRegistry(url);

		this.instanceFactory = ExtensionLoader.getExtension(InstanceFactory.class, url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON);

		return this;
	}

	@Override
	public Acceptor acceptor() {
		return acceptor;
	}

	@Override
	public Registry registry() {
		return registry;
	}

	@Override
	public void publish(ServiceConfig serviceConfig) {
		if (!currentServiceConfigs.contains(serviceConfig)) {
			registry.register(serviceConfig.url());
			instanceFactory.setInstance(serviceConfig.getInterface(), serviceConfig.getRef());
			currentServiceConfigs.add(serviceConfig);
		}
	}


	@Override
	public void publish(List<ServiceConfig> serviceConfigs) {
		for (ServiceConfig serviceConfig : serviceConfigs) {
			publish(serviceConfig);
		}
	}

	@Override
	public void publishAll() {
		for (ServiceConfig serviceConfig : currentServiceConfigs) {
			publish(serviceConfig);
		}
	}

	@Override
	public void unpublish(ServiceConfig serviceConfig) {
		if (currentServiceConfigs.contains(serviceConfig)) {
			registry.unregister(serviceConfig.url());
			currentServiceConfigs.remove(serviceConfig);
		}
	}

	@Override
	public void unpublish(List<ServiceConfig> serviceConfigs) {
		for (ServiceConfig serviceConfig : serviceConfigs) {
			unpublish(serviceConfig);
		}
	}

	@Override
	public void unpublishAll() {
		for (ServiceConfig serviceConfig : currentServiceConfigs) {
			unpublish(serviceConfig);
		}
	}

	@Override
	public void start() {
		acceptor.start();
	}

	@Override
	public void shutdownGracefully() {
		acceptor.shutdownGracefully();
		registry.close();
	}
}
