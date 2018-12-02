package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ProtocolConfig extends AbstractConfig {
	private static final long serialVersionUID = -8054840397576456746L;
	private String id;
	private String servername;
	private String serialization;
	private int heartbeat;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getSerialization() {
		return serialization;
	}

	public void setSerialization(String seralization) {
		this.serialization = seralization;
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}

	@Override
	public URL getURL() {
		if (url == null) {
			try {
				reentrantLock.lock();
				Map<String, String> parameters = new HashMap<>();
				parameters.put("id", getId());
				parameters.put("serialization", !StringUtils.isEmpty(getSerialization()) ? getSerialization() : RpcConstant.DEFAULT_SERIALIZATION);
				parameters.put("heartbeat", String.valueOf(getHeartbeat()));
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(String.format("%s://%s:%s?", RpcConstant.PROTOCOLSCHEME, NetUtils.getAvailablInetAddress().getHostAddress(),
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
