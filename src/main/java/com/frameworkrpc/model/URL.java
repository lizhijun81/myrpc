package com.frameworkrpc.model;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.exception.RpcException;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class URL implements Serializable {

	private static final long serialVersionUID = 4975402946568471616L;
	private Map<String, String> parameters;
	private volatile transient Map<String, Number> numbers;
	private URI uri;

	public URL(String url) {
		try {
			uri = new URI(url);
			setParameters();
		} catch (URISyntaxException e) {
			throw new RpcException(e.getMessage(), e);
		}
	}

	public String getScheme() {
		return uri.getScheme();
	}

	public String getHost() {
		return uri.getHost();
	}

	public int getPort() {
		return uri.getPort();
	}

	public String getPath() {
		String path = uri.getPath();
		return path.substring(1, path.length());
	}

	public String getServerPortStr() {
		return buildHostPortStr(uri.getHost(), uri.getPort());
	}

	private static String buildHostPortStr(String host, int defaultPort) {
		if (defaultPort <= 0) {
			return host;
		}

		int idx = host.indexOf(":");
		if (idx < 0) {
			return host + ":" + defaultPort;
		}

		int port = Integer.parseInt(host.substring(idx + 1));
		if (port <= 0) {
			return host.substring(0, idx + 1) + defaultPort;
		}
		return host;
	}

	public String toFullStr() {
		return this.uri.toString();
	}

	public synchronized URL addParameters(String name, String value) {
		parameters.put(name, value);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("%s://%s:%s/%s?", getScheme(), getHost(), getPort(), getPath()));
		stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));
		try {
			this.uri = new URI(stringBuilder.toString());
		} catch (URISyntaxException e) {
			throw new RpcException(e.getMessage(), e);
		}
		return this;
	}

	public synchronized void setParameters() {
		this.parameters = NetUtils.getUrlParams(uri.getQuery());
	}

	public boolean containsKey(String name) {
		return parameters.containsKey(name);
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}

	public String getParameter(String name, String defaultValue) {
		String value = getParameter(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public Boolean getBooleanParameter(String name) {
		String value = getParameter(name);
		return Boolean.parseBoolean(value);
	}

	public Boolean getBooleanParameter(String name, boolean defaultValue) {
		String value = getParameter(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}

		return Boolean.parseBoolean(value);
	}

	public Integer getIntParameter(String name) {
		Number n = getNumbers().get(name);
		if (n != null) {
			return n.intValue();
		}
		String value = parameters.get(name);
		int i = Integer.parseInt(value);
		getNumbers().put(name, i);
		return i;
	}

	public Integer getIntParameter(String name, int defaultValue) {
		Number n = getNumbers().get(name);
		if (n != null) {
			return n.intValue();
		}
		String value = parameters.get(name);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		int i = Integer.parseInt(value);
		getNumbers().put(name, i);
		return i;
	}

	private Map<String, Number> getNumbers() {
		if (numbers == null) { // 允许并发重复创建
			numbers = new ConcurrentHashMap<String, Number>();
		}
		return numbers;
	}
}
