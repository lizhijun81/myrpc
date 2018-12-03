package com.frameworkrpc.exporter;

import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistryService;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

import java.io.Serializable;

public class RpcExporter implements Serializable {

	private static final long serialVersionUID = 6568819430920801686L;
	private URL url;
	private Server server;
	private RegistryService registryService;

	public RpcExporter(URL url) {
		this.url = url;
		this.server = ServerFactory.createServer(url);
		this.registryService = RegistryFactory.createRegistry(url);
	}

	public RpcExporter openServer() {
		if (!server.isOpen()) {
			server.doOpen();
		}
		return this;
	}

	public RpcExporter exporter() {
		registryService.registerService(url);
		return this;
	}

	public RpcExporter unexport() {
		registryService.unRegisterService(url);
		return this;
	}
}
