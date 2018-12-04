package com.frameworkrpc.exporter;

import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistryService;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

import java.io.Serializable;

public class RpcExporter implements Exporter, Serializable {

	private static final long serialVersionUID = 6568819430920801686L;
	private URL url;
	private Server server;
	private RegistryService registryService;

	public RpcExporter(URL url) {
		this.url = url;
		this.server = ServerFactory.createServer(url);
		this.registryService = RegistryFactory.createRegistry(url);
	}

	@Override
	public Exporter openServer() {
		if (!server.isOpen()) {
			server.doOpen();
		}
		return this;
	}

	@Override
	public Exporter exporter() {
		registryService.registerService(url);
		return this;
	}

	@Override
	public Exporter unexport() {
		registryService.unRegisterService(url);
		return this;
	}
}
