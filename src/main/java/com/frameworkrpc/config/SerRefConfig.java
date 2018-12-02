package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class SerRefConfig<T> extends AbstractConfig {

	private static final long serialVersionUID = 5369377538761488535L;
	private String id;
	private String interfaceName;
	private Class<?> interfaceClass;
	private ApplicationConfig application;
	private RegistryConfig registry;
	private ProtocolConfig protocol;
	private String version;
	private String group;
	private String loadbalance;
	private String cluster;
	private long timeout;
	private int retries;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getInterface() {
		return interfaceName;
	}

	public void setInterface(Class<?> interfaceClass) {
		if (interfaceClass != null && !interfaceClass.isInterface()) {
			throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
		}
		this.interfaceClass = interfaceClass;
		setInterface(interfaceClass == null ? null : interfaceClass.getName());
	}

	public void setInterface(String interfaceName) {
		this.interfaceName = interfaceName;
		if (id == null || id.length() == 0) {
			id = interfaceName;
		}
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public ApplicationConfig getApplication() {
		return application;
	}

	public void setApplication(ApplicationConfig application) {
		this.application = application;
	}

	public ProtocolConfig getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolConfig protocol) {
		this.protocol = protocol;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public RegistryConfig getRegistry() {
		return registry;
	}

	public void setRegistry(RegistryConfig registry) {
		this.registry = registry;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}


	protected void getURL(String scheme) {
		try {
			reentrantLock.lock();
			Map<String, String> parameters = new HashMap<>();
			parameters.put("id", getId());
			parameters.put("interface", getInterface());
			parameters.put("version", !StringUtils.isEmpty(getVersion()) ? getVersion() : RpcConstant.DEFAULT_VERSION);
			parameters.put("group", !StringUtils.isEmpty(getGroup()) ? getGroup() : RpcConstant.DEFAULT_GROUP);
			parameters.put("loadbalance", !StringUtils.isEmpty(getLoadbalance()) ? getLoadbalance() : RpcConstant.DEFAULT_LOADBALANCE);
			parameters.put("cluster", !StringUtils.isEmpty(getCluster()) ? getCluster() : RpcConstant.DEFAULT_CLUSTER);
			parameters.put("timeout", getTimeout() > 0 ? String.valueOf(getTimeout()) : String.valueOf(RpcConstant.DEFAULT_TMEOUT));
			parameters.put("retries", getRetries() > 0 ? String.valueOf(getRetries()) : String.valueOf(RpcConstant.DEFAULT_RRETRIES));

			parameters.put("application", getApplication().getName());
			parameters.put("application.version",
					!StringUtils.isEmpty(getApplication().getVersion()) ? getApplication().getVersion() : RpcConstant.DEFAULT_VERSION);

			parameters.put("protocol", !StringUtils.isEmpty(getProtocol().getName()) ? getProtocol().getName() : RpcConstant.DEFAULT_PROTOCOL);
			parameters.put("host", getProtocol().getHost());
			parameters.put("port", String.valueOf(getProtocol().getPort()));
			parameters.put("transporter",
					!StringUtils.isEmpty(getProtocol().getTransporter()) ? getProtocol().getTransporter() : RpcConstant.DEFAULT_TRANSPORTER);
			parameters.put("serialization",
					!StringUtils.isEmpty(getProtocol().getSerialization()) ? getProtocol().getSerialization() : RpcConstant.DEFAULT_SERIALIZATION);
			parameters.put("heartbeat", String.valueOf(getProtocol().getHeartbeat()));


			parameters.put("registryname", getRegistry().getRegistryname());
			parameters.put("address", getRegistry().getAddress());
			parameters.put("registry.timeout",
					getTimeout() > 0 ? String.valueOf(getRegistry().getTimeout()) : String.valueOf(RpcConstant.DEFAULT_REGISTRY_TIMEOUT));
			parameters.put("registry.sessiontimeout", getRegistry().getSessiontimeout() > 0 ?
					String.valueOf(getRegistry().getSessiontimeout()) :
					String.valueOf(RpcConstant.DEFAULT_REGISTRY_SESSIONTIMEOUT));

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String
					.format("%s://%s:%s/%s?", scheme, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface()));
			stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));

			url = new URL(stringBuilder.toString());
		} finally {
			reentrantLock.unlock();
		}
	}

}
