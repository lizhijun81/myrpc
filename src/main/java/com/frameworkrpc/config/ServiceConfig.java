package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistryService;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

public class ServiceConfig<T> extends SerRefConfig {
	private static final long serialVersionUID = 4186914879813709242L;
	private T ref;

	public T getRef() {
		return ref;
	}

	public void setRef(T ref) {
		this.ref = ref;
	}

	public void export() {
		Server server = ServerFactory.createServer(getURL());
		if (!server.isOpen()) {
			server.doOpen();
		}
		URL registryURL = getRegistry().getURL();
		RegistryService registryService = RegistryFactory.createRegistry(registryURL);
		registryService.subscribeService(getURL());
		registryService.registerService(getURL());
	}


	public URL getURL() {
		if (url == null) {
			super.getURL(RpcConstant.PROVIDERSCHEME);
		}
		return url;
	}
}
