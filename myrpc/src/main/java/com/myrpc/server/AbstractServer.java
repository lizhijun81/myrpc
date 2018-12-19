package com.myrpc.server;

import com.myrpc.model.URL;

public abstract class AbstractServer implements Server {

	protected URL url;

	public AbstractServer(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

}
