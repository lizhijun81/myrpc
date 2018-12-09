package com.frameworkrpc.registry;

public enum RegistrySide {

	PROVIDER("provider"), CONSUMER("consumer");

	private String name;

	private RegistrySide(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static RegistrySide getRegistrySide(String name) {
		for (RegistrySide zkNodeType : RegistrySide.values()) {
			if (zkNodeType.getName().equals(name)) {
				return zkNodeType;
			}
		}
		return null;
	}
}
