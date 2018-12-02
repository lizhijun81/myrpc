package com.frameworkrpc.registry.zookeeper;

public enum ZkNodeType {

	PROVIDER("provider"),
	CONSUMER("consumer");

	private String value;

	private ZkNodeType(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
