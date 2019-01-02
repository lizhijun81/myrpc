package com.myrpc.config;

import com.myrpc.common.NetUtils;
import com.myrpc.common.RpcConstants;
import com.myrpc.exception.MyRpcRpcException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class URL implements Serializable {

	private static final long serialVersionUID = 4975402946568471616L;
	private Map<String, String> parameters;
	private volatile transient Map<String, Number> numbers;
	private URI uri;

	public URL(String url) {
		this.uri = toUrl(url);
	}

	public URL(String protocol, String host, String port, String path, Map<String, String> parameters) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("%s://%s:%s/%s?", protocol, host, port, path));
		stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));
		this.uri = toUrl(stringBuilder.toString());
	}

	private URI toUrl(String url) {
		try {
			uri = new URI(URLDecoder.decode(url, RpcConstants.CHARSET));
			setParameters();
			return uri;
		} catch (URISyntaxException e) {
			throw new MyRpcRpcException(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			throw new MyRpcRpcException(e.getMessage(), e);
		}
	}

	public String getProtocol() {
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

	public InetSocketAddress toInetSocketAddress() {
		return new InetSocketAddress(getHost(), getPort());
	}

	public String getServerPortStr() {
		return buildHostPortStr(getHost(), getPort());
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

	public String getServiceInterface() {
		return getParameter(RpcConstants.INTERFACE_KEY, getPath());
	}

	public String getServiceKey() {
		String inf = getServiceInterface();
		if (inf == null)
			return null;
		StringBuilder buf = new StringBuilder();
		String group = getParameter(RpcConstants.GROUP_KEY);
		if (group != null && group.length() > 0) {
			buf.append(group).append("/");
		}
		buf.append(inf);
		String version = getParameter(RpcConstants.VERSION_KEY);
		if (version != null && version.length() > 0) {
			buf.append(":").append(version);
		}
		return buf.toString();
	}

	public String toFullStr() {
		return this.uri.toString();
	}

	public synchronized URL addParameters(String name, String value) {
		parameters.put(name, value);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("%s://%s:%s/%s?", getProtocol(), getHost(), getPort(), getPath()));
		stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));
		try {
			this.uri = new URI(stringBuilder.toString());
		} catch (URISyntaxException e) {
			throw new MyRpcRpcException(e.getMessage(), e);
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

	public String[] getParameter(String key, String[] defaultValue) {
		String value = getParameter(key);
		if (value == null || value.length() == 0) {
			return defaultValue;
		}
		return RpcConstants.COMMA_SPLIT_PATTERN.split(value);
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
		if (numbers == null) {
			numbers = new ConcurrentHashMap<>();
		}
		return numbers;
	}

	public static String encode(String value) {
		if (value == null || value.length() == 0) {
			return "";
		}
		try {
			return URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static String decode(String value) {
		if (value == null || value.length() == 0) {
			return "";
		}
		try {
			return URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static List<String> parseAddress(String address) {
		if (address == null || address.length() == 0) {
			return null;
		}
		String[] addresses = RpcConstants.REGISTRY_SPLIT_PATTERN.split(address);
		if (addresses == null || addresses.length == 0) {
			return null; //here won't be empty
		}
		return Arrays.asList(addresses);
	}

	@Override
	public String toString() {
		return toFullStr();
	}
}
