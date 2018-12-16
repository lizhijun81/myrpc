package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstants;

import java.util.Map;

public class ConsumerConfig<T> extends ProviderConfig<T> {

	private static final long serialVersionUID = 5369377538761488535L;
	protected int retries;
	protected int connecttimeout;
	protected String proxy;
	protected String cluster;
	protected String loadbalance;

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		this.connecttimeout = connecttimeout;
	}

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}


	protected void addConsumerParameters(Map<String, String> parameters) {
		parameters.put(RpcConstants.LOADBALANCE_KEY, getVal(getLoadbalance(), RpcConstants.DEFAULT_LOADBALANCE));
		parameters.put(RpcConstants.RETRIES_KEY, getVal(getRetries(), RpcConstants.DEFAULT_RRETRIES));
		parameters.put(RpcConstants.CONNECTTIMEOUT_KEY, getVal(getConnecttimeout(), RpcConstants.DEFAULT_CONNECTTIMEOUT));
		parameters.put(RpcConstants.PROXY_KEY, getVal(getProxy(), RpcConstants.DEFAULT_PROXY));
		parameters.put(RpcConstants.CLUSTER_KEY, getVal(getCluster(), RpcConstants.DEFAULT_CLUSTER));
	}
}
