package com.myrpc.config;

import com.myrpc.common.RpcConstants;
import com.myrpc.common.StringUtils;

import java.util.Map;

public class RegistryConfig extends AbstractConfig {

	private static final long serialVersionUID = -8315812157243230679L;
	private String name;
	private String address;
	private int timeout;
	private int sessiontimeout;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	protected void addRegistryParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getName())) {
			throw new IllegalStateException("registry.name can not be empty");
		}
		if (StringUtils.isEmpty(getAddress())) {
			throw new IllegalStateException("registry.address can not be empty");
		}
		parameters.put(RpcConstants.REGISTRY_NAME_KEY, getName());
		parameters.put(RpcConstants.REGISTRY_ADDRESS_KEY, getAddress());
		parameters.put(RpcConstants.REGISTRY_TIMEOUT_KEY, getVal(getTimeout(), RpcConstants.DEFAULT_REGISTRY_TIMEOUT));
		parameters.put(RpcConstants.REGISTRY_SESSIONTIMEOUT_KEY,
				getVal(getSessiontimeout(), RpcConstants.DEFAULT_REGISTRY_SESSIONTIMEOUT));
	}
}
