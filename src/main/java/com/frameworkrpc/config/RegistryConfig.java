package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryService;

import java.util.HashMap;
import java.util.Map;

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

	public URL getURL() {
		if (url == null) {
			try {
				reentrantLock.lock();
				Map<String, String> parameters = new HashMap<>();
				parameters.put("id", getId());
				parameters.put("registryname", getRegistryname());
				parameters.put("address", getAddress());
				parameters.put("registry.timeout",
						getTimeout() > 0 ? String.valueOf(getTimeout()) : String.valueOf(RpcConstant.DEFAULT_REGISTRY_TIMEOUT));
				parameters.put("registry.sessiontimeout",
						getSessiontimeout() > 0 ? String.valueOf(getSessiontimeout()) : String.valueOf(RpcConstant.DEFAULT_REGISTRY_SESSIONTIMEOUT));

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(String.format("%s://%s:%s/%s?", RpcConstant.REGISTRYSCHEME, NetUtils.getAvailablInetAddress().getHostAddress(),
						NetUtils.getAvailablePort(), RegistryService.class.getName()));
				stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));

				url = new URL(stringBuilder.toString());
			} finally {
				reentrantLock.unlock();
			}
		}
		return url;
	}
}
