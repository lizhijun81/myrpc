package com.frameworkrpc.exporter.defaults;

import com.frameworkrpc.annotation.RpcComponent;
import com.frameworkrpc.exporter.AbstractExporter;
import com.frameworkrpc.exporter.Exporter;

@RpcComponent(name = "default")
public class DefaultExporter extends AbstractExporter implements Exporter {

	private static final long serialVersionUID = 6568819430920801686L;

	@Override
	public void exportServer() {
		if (!exportedServers.containsKey(url.getServerPortStr())) {
			exportedServers.put(url.getServerPortStr(), server);
		} else {
			server = exportedServers.get(url.getServerPortStr());
		}
		if (!server.isOpened()) {
			server.doOpen();
		}
	}

	@Override
	public void exportUrl() {
		if (!exportedUrls.contains(url)) {
			registry.registerService(url);
			exportedUrls.add(url);
		}
	}

	@Override
	public void unexportServer() {
		if (!server.isClosed()) {
			server.doClose();
		}
		if (exportedServers.containsKey(url.getServerPortStr())) {
			exportedServers.remove(url.getServerPortStr());
		}
	}

	@Override
	public void unexportUrl() {
		if (exportedUrls.contains(url)) {
			registry.unRegisterService(url);
			exportedUrls.remove(url);
		}
	}
}
