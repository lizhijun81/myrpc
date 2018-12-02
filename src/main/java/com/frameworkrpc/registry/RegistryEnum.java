package com.frameworkrpc.registry;

public enum RegistryEnum {

	Zookeeper("zookeeper");

	private String registryname;

	public String getRegistryname() {
		return registryname;
	}

	public void setRegistryname(String registryname) {
		this.registryname = registryname;
	}

	RegistryEnum(String registryname) {
		this.registryname = registryname;
	}

	public static RegistryEnum getRegistryEnum(String registryname) {
		for (RegistryEnum registryEnum : RegistryEnum.values()) {
			if (registryEnum.getRegistryname().equals(registryname)) {
				return registryEnum;
			}
		}
		return null;
	}
}
