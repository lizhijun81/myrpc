package com.myrpc.transport;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;

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
	public int getConnectTimeout() {
		return url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY);
	}
}
