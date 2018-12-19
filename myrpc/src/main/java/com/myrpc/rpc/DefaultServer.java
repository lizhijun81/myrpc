package com.myrpc.rpc;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.ServiceConfig;
import com.myrpc.config.URL;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.extension.Scope;
import com.myrpc.registry.Registry;
import com.myrpc.transport.Acceptor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServer implements Server {

	private static final Map<String, ServiceConfig> currentSerives = new ConcurrentHashMap<>();
	private URL url;
	private Acceptor acceptor;
	private Registry registry;
	private InstanceFactory instanceFactory;

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
	public Server with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public Server init() {

		this.acceptor = ExtensionLoader.getExtensionLoader(Acceptor.class).getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY)).with(url)
				.init();

		this.registry = ExtensionLoader.getExtensionLoader(Registry.class).getExtension(url.getParameter(RpcConstants.REGISTRY_NAME_KEY)).with(url)
				.init();

		this.instanceFactory = ExtensionLoader.getExtensionLoader(InstanceFactory.class)
				.getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON);

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
		if (!currentSerives.containsKey(serviceConfig.url().toFullStr())) {
			registry.register(serviceConfig.url());
			instanceFactory.setInstance(serviceConfig.getInterface(), serviceConfig.getRef());
			currentSerives.put(serviceConfig.url().toFullStr(), serviceConfig);
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
		for (ServiceConfig serviceConfig : currentSerives.values()) {
			publish(serviceConfig);
		}
	}

	@Override
	public void unpublish(ServiceConfig serviceConfig) {
		if (currentSerives.containsKey(serviceConfig.url().toFullStr())) {
			registry.unregister(serviceConfig.url());
			currentSerives.remove(serviceConfig.url().toFullStr());
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
		for (ServiceConfig serviceConfig : currentSerives.values()) {
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
