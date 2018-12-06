package com.frameworkrpc.proxy;


public enum RpcProxyEnum {
	Jdk("jdk");

	private String proxyname;

	public String getProxyname() {
		return proxyname;
	}

	public void setProxyname(String proxyname) {
		this.proxyname = proxyname;
	}


	RpcProxyEnum(String proxyname) {
		this.proxyname = proxyname;
	}

	public static RpcProxyEnum getRpcProxyEnum(String proxyname) {
		for (RpcProxyEnum ppcProxyEnum : RpcProxyEnum.values()) {
			if (ppcProxyEnum.getProxyname().equals(proxyname)) {
				return ppcProxyEnum;
			}
		}
		return null;
	}
}
