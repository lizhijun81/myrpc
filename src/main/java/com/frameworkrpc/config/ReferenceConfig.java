package com.frameworkrpc.config;

public class ReferenceConfig<T> extends SerRefConfig {

	private static final long serialVersionUID = 8866752725969090439L;
	protected int connecttimeout;

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		this.connecttimeout = connecttimeout;
	}


	public T get() {
		return null;
	}

	;
}
