package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig extends AbstractConfig {
	private static final long serialVersionUID = -1844587035484060730L;
	private String id;
	private String name;
	private String version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	public URL getURL() {
		if (url == null) {
			try {
				reentrantLock.lock();
				Map<String, String> parameters = new HashMap<>();
				parameters.put("id", getId());
				parameters.put("name", getName());
				parameters.put("application.version", !StringUtils.isEmpty(getVersion()) ? getVersion() : RpcConstant.DEFAULT_VERSION);
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(String.format("%s://%s:%s?", RpcConstant.APPLICATIONSCHEME, NetUtils.getAvailablInetAddress().getHostAddress(),
						NetUtils.getAvailablePort()));
				stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));

				url = new URL(stringBuilder.toString());
			} finally {
				reentrantLock.unlock();
			}
		}
		return url;
	}
}
