package com.frameworkrpc.client;

public enum ChannelClientEnum {

	Netty("netty");

	private String clientname;

	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	ChannelClientEnum(String clientname) {
		this.clientname = clientname;
	}

	public static ChannelClientEnum getClientEnumm(String clientname) {
		for (ChannelClientEnum clientEnum : ChannelClientEnum.values()) {
			if (clientEnum.getClientname().equals(clientname)) {
				return clientEnum;
			}
		}
		return null;
	}
}
