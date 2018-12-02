package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;

public class ReferenceConfig<T> extends SerRefConfig {
	private static final long serialVersionUID = 8866752725969090439L;

	public URL getURL() {
		if (url == null) {
			super.getURL(RpcConstant.CONSUMERSCHEME);
		}
		return url;
	}
}
