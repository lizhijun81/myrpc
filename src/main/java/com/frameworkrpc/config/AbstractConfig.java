package com.frameworkrpc.config;

import java.io.Serializable;

public abstract class AbstractConfig implements Serializable {

	private static final long serialVersionUID = 4004312429333017685L;
	protected String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

}
