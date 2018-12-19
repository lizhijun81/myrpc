package com.myrpc.client;

import com.myrpc.common.RpcConstants;
import com.myrpc.model.URL;

import java.net.InetSocketAddress;

public abstract class AbstractClient implements Client {

	protected volatile boolean isClosed;
	protected URL url;

	public AbstractClient(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}

	public InetSocketAddress getConnectAddress() {
		return new InetSocketAddress(url.getHost(), url.getPort());
	}

	protected int getConnectTimeout() {
		return url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY);
	}


}
