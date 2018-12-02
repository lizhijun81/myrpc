package com.frameworkrpc.server;

import com.frameworkrpc.model.URL;

public class AbstractServer {

	protected volatile boolean isOpen;
	protected volatile boolean isClose;
	protected URL url;

	public AbstractServer(URL url) {
		this.url = url;
	}

	public URL getURL() {
		return url;
	}


}
