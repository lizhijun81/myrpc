package com.myrpc.transport;

import com.myrpc.config.URL;

import java.net.SocketAddress;

public abstract class AbstractAcceptor implements Acceptor {

	protected URL url;

	@Override
	public URL url() {
		return url;
	}

	@Override
	public Acceptor with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public SocketAddress localAddress() {
		return url.toInetSocketAddress();
	}
}
