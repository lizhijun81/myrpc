package com.frameworkrpc.exporter;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

import java.io.Serializable;

public class Exporter implements Serializable {

	private static final long serialVersionUID = -6374024642709901255L;
	protected static Server server;
	protected URL url;
	protected Registry registry;

	public Exporter(URL url) {
		this.url = url;
		if (server == null) {
			synchronized (this.getClass()) {
				if (server == null) {
					server = ExtensionLoader.getExtensionLoader(ServerFactory.class)
							.getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON).getServer(url);
				}
			}
		}

		this.registry = ExtensionLoader.getExtensionLoader(RegistryFactory.class)
				.getExtension(url.getParameter(RpcConstants.REGISTRY_NAME_KEY), Scope.SINGLETON).getRegistry(url);
	}

	public Exporter exportServer() {
		synchronized (this.getClass()) {
			if (!server.isOpened()) {
				server.doOpen();
			}
		}
		return this;
	}


	public Exporter exportUrl() {
		URL registerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		registry.register(registerUrl);
		return this;
	}


	public Exporter unexportServer() {
		if (!server.isClosed()) {
			server.doClose();
		}
		return this;
	}


	public Exporter unexportUrl() {
		URL registerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		registry.unregister(registerUrl);
		return this;
	}
}
