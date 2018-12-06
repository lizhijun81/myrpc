package com.frameworkrpc.server;

public enum ChannelServerEnum {

	Netty("netty");

	private String servername;

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	ChannelServerEnum(String servername) {
		this.servername = servername;
	}

	public static ChannelServerEnum getServerEnum(String servername) {
		for (ChannelServerEnum serverEnum : ChannelServerEnum.values()) {
			if (serverEnum.getServername().equals(servername)) {
				return serverEnum;
			}
		}
		return null;
	}
}
