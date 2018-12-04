package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.common.StringUtils;
import com.frameworkrpc.exporter.Exporter;
import com.frameworkrpc.model.URL;

import java.util.HashMap;
import java.util.Map;

public class SerRefConfig<T> extends AbstractConfig {

	private static final long serialVersionUID = 5369377538761488535L;
	protected String id;
	protected String interfaceName;
	protected Class<?> interfaceClass;
	protected ApplicationConfig application;
	protected RegistryConfig registry;
	protected ProtocolConfig protocol;
	protected String version;
	protected String group;
	protected String loadbalance;
	protected String cluster;
	protected long timeout;
	protected int retries;
	protected Exporter exporter;

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

	private volatile URL url;

	private volatile URL registryURL;

	private volatile URL protocolURL;

	protected URL getURL() {
		if (url != null)
			return url;
		synchronized (this) {
			if (url != null)
				return url;
			Map<String, String> parameters = new HashMap<>();
			parameters.put("id", getId());
			addServiceParameters(parameters);
			addAppliactionParameters(parameters);
			addProtocolParameters(parameters);
			addRegistryParameters(parameters);

			String scheme = this.getClass().getName().contains("Service") ? RpcConstant.PROVIDERSCHEME : RpcConstant.CONSUMERSCHEME;
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String
					.format("%s://%s:%s/%s?", scheme, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface()));
			stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));

			url = new URL(stringBuilder.toString());
		}
		return url;
	}

	private void addServiceParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getInterface())) {
			throw new IllegalStateException("interfaceName can not be empty");
		}
		parameters.put("interface", getInterface());
		parameters.put("version", !StringUtils.isEmpty(getVersion()) ? getVersion() : RpcConstant.DEFAULT_VERSION);
		parameters.put("group", !StringUtils.isEmpty(getGroup()) ? getGroup() : RpcConstant.DEFAULT_GROUP);
		parameters.put("loadbalance", !StringUtils.isEmpty(getLoadbalance()) ? getLoadbalance() : RpcConstant.DEFAULT_LOADBALANCE);
		parameters.put("cluster", !StringUtils.isEmpty(getCluster()) ? getCluster() : RpcConstant.DEFAULT_CLUSTER);
		parameters.put("timeout", getTimeout() > 0 ? String.valueOf(getTimeout()) : String.valueOf(RpcConstant.DEFAULT_TMEOUT));
		parameters.put("retries", getRetries() > 0 ? String.valueOf(getRetries()) : String.valueOf(RpcConstant.DEFAULT_RRETRIES));
	}

	private void addAppliactionParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getApplication().getName())) {
			throw new IllegalStateException("applicationName can not be empty");
		}
		parameters.put("application", getApplication().getName());
		parameters.put("application.version",
				!StringUtils.isEmpty(getApplication().getVersion()) ? getApplication().getVersion() : RpcConstant.DEFAULT_VERSION);
	}

	private void addProtocolParameters(Map<String, String> parameters) {
		parameters.put("protocol", !StringUtils.isEmpty(getProtocol().getName()) ? getProtocol().getName() : RpcConstant.DEFAULT_PROTOCOL);
		parameters.put("host", getProtocol().getHost());
		parameters.put("port", String.valueOf(getProtocol().getPort()));
		parameters.put("transporter",
				!StringUtils.isEmpty(getProtocol().getTransporter()) ? getProtocol().getTransporter() : RpcConstant.DEFAULT_TRANSPORTER);
		parameters.put("serialization",
				!StringUtils.isEmpty(getProtocol().getSerialization()) ? getProtocol().getSerialization() : RpcConstant.DEFAULT_SERIALIZATION);
		parameters.put("heartbeat", String.valueOf(getProtocol().getHeartbeat()));

		parameters.put("threadpool",
				!StringUtils.isEmpty(getProtocol().getThreadpool()) ? getProtocol().getThreadpool() : RpcConstant.DEFAULT_THREADPOOL);
		parameters.put("threads",
				getProtocol().getThreads() > 0 ? String.valueOf(getProtocol().getThreads()) : String.valueOf(RpcConstant.DEFAULT_THREADS));
		parameters.put("iothreads",
				getProtocol().getIothreads() > 0 ? String.valueOf(getProtocol().getIothreads()) : String.valueOf(RpcConstant.DEFAULT_IOTHREADS));
	}

	private void addRegistryParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getRegistry().getRegistryname())) {
			throw new IllegalStateException("registryname can not be empty");
		}
		if (StringUtils.isEmpty(getRegistry().getAddress())) {
			throw new IllegalStateException("registryaddress can not be empty");
		}
		parameters.put("registryname", getRegistry().getRegistryname());
		parameters.put("address", getRegistry().getAddress());
		parameters.put("registry.timeout",
				getRegistry().getTimeout() > 0 ? String.valueOf(getRegistry().getTimeout()) : String.valueOf(RpcConstant.DEFAULT_REGISTRY_TIMEOUT));
		parameters.put("registry.sessiontimeout", getRegistry().getSessiontimeout() > 0 ?
				String.valueOf(getRegistry().getSessiontimeout()) :
				String.valueOf(RpcConstant.DEFAULT_REGISTRY_SESSIONTIMEOUT));
	}
}
