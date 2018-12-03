package com.frameworkrpc.config;

public class RegistryConfig extends AbstractConfig {
	private static final long serialVersionUID = -8315812157243230679L;
	private String id;
	private String registryname;
	private String address;
	private int timeout;
	private int sessiontimeout;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegistryname() {
		return registryname;
	}

	public void setRegistryname(String registryname) {
		this.registryname = registryname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getSessiontimeout() {
		return sessiontimeout;
	}

	public void setSessiontimeout(int sessiontimeout) {
		this.sessiontimeout = sessiontimeout;
	}

}
