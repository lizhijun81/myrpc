package com.frameworkrpc.client;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;

import java.net.InetSocketAddress;

public class AbstractClient {

	protected volatile boolean isOpen;
	protected volatile boolean isClosed;
	protected volatile boolean isConnected;
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
		return url.getIntParameter(RpcConstant.CONNECTTIMEOUT);
	}


}
