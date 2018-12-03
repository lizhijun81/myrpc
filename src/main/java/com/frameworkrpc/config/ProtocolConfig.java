package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.StringUtils;

public class ProtocolConfig extends AbstractConfig {

	private static final long serialVersionUID = -8054840397576456746L;
	private String id;
	private String name;
	private String host;
	private int port;
	private String serialization;
	private String transporter;
	private int heartbeat;

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

	public synchronized String getHost() {
		if (StringUtils.isEmpty(this.host)) {
			this.host = NetUtils.getAvailablInetAddress().getHostAddress();
		}
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public synchronized int getPort() {
		if (this.port <= 0) {
			this.port = NetUtils.getAvailablePort();
		}
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSerialization() {
		return serialization;
	}

	public void setSerialization(String seralization) {
		this.serialization = seralization;
	}

	public String getTransporter() {
		return transporter;
	}

	public void setTransporter(String transporter) {
		this.transporter = transporter;
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}

}