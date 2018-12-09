package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.ProxyFactory;

public class ReferenceConfig<T> extends ExporterConfig {

	private static final long serialVersionUID = 8866752725969090439L;
	protected int connecttimeout;

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		this.connecttimeout = connecttimeout;
	}

	@Override
	protected URL getUrl() {
		URL url = super.getUrl();
		return url.addParameters("connecttimeout",
				getConnecttimeout() > 0 ? String.valueOf(getConnecttimeout()) : String.valueOf(RpcConstant.DEFAULT_CONNECTTIMEOUT));
	}

	public T get() {
		ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension("jdk");
		proxyFactory.initProxy(getUrl());
		return proxyFactory.getInstance((Class<T>) interfaceClass, getUrl());
	}

}
