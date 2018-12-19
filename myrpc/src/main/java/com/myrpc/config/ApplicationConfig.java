package com.myrpc.config;

public class ApplicationConfig extends AbstractConfig {

	private static final long serialVersionUID = -1844587035484060730L;
	private String name;
	private String version;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
