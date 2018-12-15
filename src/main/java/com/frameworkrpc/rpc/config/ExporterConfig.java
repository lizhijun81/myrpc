package com.frameworkrpc.rpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.common.StringUtils;
import com.frameworkrpc.model.URL;

import java.util.HashMap;
import java.util.Map;

public class ExporterConfig<T> extends AbstractConfig {

	private static final long serialVersionUID = 5369377538761488535L;
	protected String interfaceName;
	protected Class<T> interfaceClass;
	protected volatile T ref;
	protected ApplicationConfig application;
	protected RegistryConfig registry;
	protected ProtocolConfig protocol;
	protected String version;
	protected String group;
	protected String loadbalance;
	protected int timeout;
	protected int retries;
	protected int connecttimeout;
	protected String proxy;
	protected String cluser;


	public String getInterface() {
		return interfaceName;
	}

	public void setInterface(Class<T> interfaceClass) {
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
		if (interfaceClass != null) {
			return interfaceClass;
		}
		try {
			if (interfaceName != null && interfaceName.length() > 0) {
				this.interfaceClass = (Class<T>) Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
			}
		} catch (ClassNotFoundException t) {
			throw new IllegalStateException(t.getMessage(), t);
		}
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public T getRef() {
		return ref;
	}

	public void setRef(T ref) {
		this.ref = ref;
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

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
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

	public String getCluser() {
		return cluser;
	}

	public void setCluser(String cluser) {
		this.cluser = cluser;
	}


	private volatile URL url;

	protected URL getUrl() {
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

			String protocol = this.getClass().getName().contains("Service") ? RpcConstants.PROVIDER : RpcConstants.CONSUMER;
			if (protocol.equals(RpcConstants.CONSUMER)) {
				parameters.put("connecttimeout",
						getConnecttimeout() > 0 ? String.valueOf(getConnecttimeout()) : String.valueOf(RpcConstants.DEFAULT_CONNECTTIMEOUT));
				parameters.put("proxy", !StringUtils.isEmpty(getProxy()) ? getProxy() : RpcConstants.DEFAULT_PROXY);
				parameters.put("cluster", !StringUtils.isEmpty(getCluser()) ? getCluser() : RpcConstants.DEFAULT_CLUSTER);
			}
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String
					.format("%s://%s:%s/%s?", protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface()));
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
		parameters.put("version", !StringUtils.isEmpty(getVersion()) ? getVersion() : RpcConstants.DEFAULT_VERSION);
		parameters.put("group", !StringUtils.isEmpty(getGroup()) ? getGroup() : RpcConstants.DEFAULT_GROUP);
		parameters.put("loadbalance", !StringUtils.isEmpty(getLoadbalance()) ? getLoadbalance() : RpcConstants.DEFAULT_LOADBALANCE);
		parameters.put("timeout", getTimeout() > 0 ? String.valueOf(getTimeout()) : String.valueOf(RpcConstants.DEFAULT_TMEOUT));
		parameters.put("retries", getRetries() > 0 ? String.valueOf(getRetries()) : String.valueOf(RpcConstants.DEFAULT_RRETRIES));
	}

	private void addAppliactionParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getApplication().getName())) {
			throw new IllegalStateException("applicationName can not be empty");
		}
		parameters.put("application", getApplication().getName());
		parameters.put("application.version",
				!StringUtils.isEmpty(getApplication().getVersion()) ? getApplication().getVersion() : RpcConstants.DEFAULT_VERSION);
	}

	private void addProtocolParameters(Map<String, String> parameters) {
		if (this.protocol == null)
			this.protocol = new ProtocolConfig();
		parameters.put("protocol", !StringUtils.isEmpty(getProtocol().getName()) ? getProtocol().getName() : RpcConstants.DEFAULT_PROTOCOL);
		parameters.put("host", getProtocol().getHost());
		parameters.put("port", String.valueOf(getProtocol().getPort()));
		parameters.put("transporter",
				!StringUtils.isEmpty(getProtocol().getTransporter()) ? getProtocol().getTransporter() : RpcConstants.DEFAULT_TRANSPORTER);
		parameters.put("serialization",
				!StringUtils.isEmpty(getProtocol().getSerialization()) ? getProtocol().getSerialization() : RpcConstants.DEFAULT_SERIALIZATION);
		parameters.put("heartbeat",
				getProtocol().getHeartbeat() > 0 ? String.valueOf(getProtocol().getHeartbeat()) : String.valueOf(RpcConstants.DEFAULT_HEARTBEAT));
		parameters.put("threadpool",
				!StringUtils.isEmpty(getProtocol().getThreadpool()) ? getProtocol().getThreadpool() : RpcConstants.DEFAULT_THREADPOOL);
		parameters.put("threads",
				getProtocol().getThreads() > 0 ? String.valueOf(getProtocol().getThreads()) : String.valueOf(RpcConstants.DEFAULT_THREADS));
		parameters.put("iothreads",
				getProtocol().getIothreads() > 0 ? String.valueOf(getProtocol().getIothreads()) : String.valueOf(RpcConstants.DEFAULT_IOTHREADS));
	}

	private void addRegistryParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getRegistry().getName())) {
			throw new IllegalStateException("registry.name can not be empty");
		}
		if (StringUtils.isEmpty(getRegistry().getAddress())) {
			throw new IllegalStateException("registry.address can not be empty");
		}
		parameters.put("registry.name", getRegistry().getName());
		parameters.put("address", getRegistry().getAddress());
		parameters.put("registry.timeout",
				getRegistry().getTimeout() > 0 ? String.valueOf(getRegistry().getTimeout()) : String.valueOf(RpcConstants.DEFAULT_REGISTRY_TIMEOUT));
		parameters.put("registry.sessiontimeout", getRegistry().getSessiontimeout() > 0 ?
				String.valueOf(getRegistry().getSessiontimeout()) :
				String.valueOf(RpcConstants.DEFAULT_REGISTRY_SESSIONTIMEOUT));
	}
}
