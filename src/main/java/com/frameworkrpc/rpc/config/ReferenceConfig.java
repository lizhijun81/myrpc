package com.frameworkrpc.rpc.config;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.consumer.proxy.ClassProxy;
import com.frameworkrpc.extension.ExtensionLoader;

public class ReferenceConfig<T> extends ExporterConfig<T> {

	private static final long serialVersionUID = 8866752725969090439L;

	public T get() {
		if (ref == null) {
			synchronized (this) {
				if (ref == null) {
					ClassProxy classProxy = ExtensionLoader.getExtensionLoader(ClassProxy.class)
							.getExtension(getUrl().getParameter(RpcConstants.PROXY_KEY)).with(getUrl()).init();
					ref = classProxy.getInstance(interfaceClass);
				}
			}
		}
		return ref;
	}

}
