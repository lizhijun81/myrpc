package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;

public class ReferenceConfig<T> extends ExporterConfig {

	private static final long serialVersionUID = 8866752725969090439L;
	protected int connecttimeout;

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		this.connecttimeout = connecttimeout;
	}

	@Override
	protected URL getUrl() {
		URL url = super.getUrl();
		return url.addParameters("connecttimeout",
				getConnecttimeout() > 0 ? String.valueOf(getConnecttimeout()) : String.valueOf(RpcConstant.DEFAULT_CONNECTTIMEOUT));
	}

	public T get() {
		return null;
	}

	;
}
