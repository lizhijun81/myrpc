package com.myrpc.config;

import com.myrpc.common.RpcConstants;
import com.myrpc.common.StringUtils;

import java.util.Map;

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

	protected void addAppliactionParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getName())) {
			throw new IllegalStateException("applicationName can not be empty");
		}
		parameters.put(RpcConstants.APPLICATION_KEY, getName());
		parameters.put(RpcConstants.APPLICATION_VERSION_KEY, getVal(getVersion(), RpcConstants.DEFAULT_VERSION));
	}
}
