package com.frameworkrpc.exporter;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.URL;

@RpcComponent(name = "default")
public class DefaultExporter extends AbstractExporter implements Exporter {

	private static final long serialVersionUID = 6568819430920801686L;

	public DefaultExporter(URL url) {
		super(url);
	}

	@Override
	public void exportServer() {
		if (!server.isOpened()) {
			server.doOpen();
		}
	}

	@Override
	public void exportUrl() {
		URL registerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		registry.register(registerUrl);
	}

	@Override
	public void unexportServer() {
		if (!server.isClosed()) {
			server.doClose();
		}
	}

	@Override
	public void unexportUrl() {
		URL registerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		registry.unregister(registerUrl);
	}
}
