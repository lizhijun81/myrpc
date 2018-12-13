package com.frameworkrpc.config;

import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.proxy.ClassProxy;
import com.frameworkrpc.proxy.ProxyFactory;

public class ReferenceConfig<T> extends ExporterConfig<T> {

	private static final long serialVersionUID = 8866752725969090439L;

	public T get() {
		if (ref == null) {
			ClassProxy proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension("jdk", Scope.SINGLETON)
					.getClassProxy(getUrl());
			ref = proxyFactory.getInstance(interfaceClass);
		}
		return ref;
	}

}
