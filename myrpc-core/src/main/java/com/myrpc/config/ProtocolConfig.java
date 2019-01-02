package com.myrpc.config;

import com.myrpc.common.NetUtils;
import com.myrpc.common.RpcConstants;
import com.myrpc.common.StringUtils;

import java.util.Map;

public class ProtocolConfig extends AbstractConfig {

	private static final long serialVersionUID = -8054840397576456746L;
	private String name;
	private String host;
	private int port;
	private String serialization;
	private String transporter;
	private int heartbeat;
	private String threadpool;
	private int threads;
	private int iothreads;


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

	public String getThreadpool() {
		return threadpool;
	}

	public void setThreadpool(String threadpool) {
		this.threadpool = threadpool;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int getIothreads() {
		return iothreads;
	}

	public void setIothreads(int iothreads) {
		this.iothreads = iothreads;
	}


	protected void addProtocolParameters(Map<String, String> parameters) {
		parameters.put(RpcConstants.PROTOCOL_KEY, getVal(getName(), RpcConstants.DEFAULT_PROTOCOL));
		parameters.put(RpcConstants.HOST_KEY, getHost());
		parameters.put(RpcConstants.PORT_KEY, String.valueOf(getPort()));
		parameters.put(RpcConstants.TRANSPORTER_KEY, getVal(getTransporter(), RpcConstants.DEFAULT_TRANSPORTER));
		parameters.put(RpcConstants.SERIALIZATION_KEY, getVal(getSerialization(), RpcConstants.DEFAULT_SERIALIZATION));
		parameters.put(RpcConstants.HEARTBEAT_KEY, getVal(getHeartbeat(), RpcConstants.DEFAULT_HEARTBEAT));
		parameters.put(RpcConstants.THREADPOOL_KEY, getVal(getThreadpool(), RpcConstants.DEFAULT_THREADPOOL));
		parameters.put(RpcConstants.THREADS_KEY, getVal(getThreads(), RpcConstants.DEFAULT_THREADS));
		parameters.put(RpcConstants.IOTHREADS_KEY, getVal(getIothreads(), RpcConstants.DEFAULT_IOTHREADS));
	}
}
