package com.myrpc.transport;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;

import java.net.InetSocketAddress;

public abstract class AbstractConnector implements Connector {

	protected URL url;

	@Override
	public URL url() {
		return url;
	}

	@Override
	public Connector with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public InetSocketAddress getConnectAddress() {
		return new InetSocketAddress(url.getHost(), url.getPort());
	}

	@Override
	public int getConnectTimeout() {
		return url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY);
	}
}
