package com.frameworkrpc.server;

public enum ServerEnum {

	Netty("netty");

	private String servername;

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	ServerEnum(String servername) {
		this.servername = servername;
	}

	public static ServerEnum getServerEnum(String servername) {
		for (ServerEnum serverEnum : ServerEnum.values()) {
			if (serverEnum.getServername().equals(servername)) {
				return serverEnum;
			}
		}
		return null;
	}
}
