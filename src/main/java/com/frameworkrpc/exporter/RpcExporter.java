package com.frameworkrpc.exporter;

import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistryService;
import com.frameworkrpc.server.ChannelServer;
import com.frameworkrpc.server.ChannelServerFactory;

import java.io.Serializable;

public class RpcExporter implements Exporter, Serializable {

	private static final long serialVersionUID = 6568819430920801686L;
	private URL url;
	private ChannelServer server;
	private RegistryService registryService;

	public RpcExporter(URL url) {
		this.url = url;
		this.server = ChannelServerFactory.createServer(url);
		this.registryService = RegistryFactory.createRegistry(url);
	}

	@Override
	public Exporter export() {
		if (!server.isOpened()) {
			server.doOpen();
		}
		registryService.registerService(url);
		return this;
	}

	@Override
	public Exporter unexport() {
		if (!server.isClosed()) {
			server.doClose();
		}
		registryService.unRegisterService(url);
		return this;
	}
}
