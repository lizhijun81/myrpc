package com.frameworkrpc.exporter.defaults;

import com.frameworkrpc.exporter.AbstractExporter;
import com.frameworkrpc.exporter.Exporter;
import com.frameworkrpc.extension.RpcComponent;

@RpcComponent(name = "default")
public class DefaultExporter extends AbstractExporter implements Exporter {

	private static final long serialVersionUID = 6568819430920801686L;

	@Override
	public void exportServer() {
		if (!server.isOpened()) {
			server.doOpen();
		}
	}

	@Override
	public void exportUrl() {
		if (!exportedUrls.contains(url)) {
			registry.register(url);
			exportedUrls.add(url);
		}
	}

	@Override
	public void unexportServer() {
		if (!server.isClosed()) {
			server.doClose();
		}
		exportedServers.remove(url.getServerPortStr());
	}

	@Override
	public void unexportUrl() {
		registry.unregister(url);
		exportedUrls.remove(url);
	}
}
