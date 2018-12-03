package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistryService;
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
			String scheme = this.getClass().getName().contains("Service") ? RpcConstant.PROVIDERSCHEME : RpcConstant.CONSUMERSCHEME;
			if (scheme.equals(RpcConstant.PROVIDERSCHEME)) {

			}
			exportService(parameters);
			exportAppliaction(parameters);
			exportProtocol(parameters);
			exportRegistry(parameters);
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String
					.format("%s://%s:%s/%s?", scheme, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface()));
			stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));

			url = new URL(stringBuilder.toString());
		}
		return url;
	}

	public URL getProtocolURL() {
		if (protocolURL != null)
			return protocolURL;
		synchronized (this) {
			if (protocolURL != null)
				return protocolURL;
			Map<String, String> parameters = new HashMap<>();
			parameters.put("id", getProtocol().getId());
			exportProtocol(parameters);

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String
					.format("%s://%s:%s/%s?", RpcConstant.PROTOCOLSCHEME, getProtocol().getHost(), String.valueOf(getProtocol().getPort()),
							getProtocol().getName()));
			stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));
			protocolURL = new URL(stringBuilder.toString());
		}
		return protocolURL;
	}

	public URL getRegistryURL() {
		if (registryURL != null)
			return registryURL;
		synchronized (this) {
			if (registryURL != null)
				return registryURL;
			Map<String, String> parameters = new HashMap<>();
			parameters.put("id", getRegistry().getId());
			exportRegistry(parameters);

			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(String
					.format("%s://%s:%s/%s?", RpcConstant.REGISTRYSCHEME, getProtocol().getHost(), String.valueOf(getProtocol().getPort()),
							RegistryService.class.getName()));
			stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));
			//			try {
			//				stringBuilder.append(String.format("&export%s=", URLEncoder.encode(getURL().toFullStr(), RpcConstant.CHARSET)));
			//			} catch (UnsupportedEncodingException e) {
			//				throw new CommonRpcException(e.getMessage(), e);
			//			}
			registryURL = new URL(stringBuilder.toString());
		}
		return registryURL;
	}

	private void exportService(Map<String, String> parameters) {
		if (!StringUtils.isEmpty(getInterface())) {
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

	private void exportAppliaction(Map<String, String> parameters) {
		if (!StringUtils.isEmpty(getApplication().getName())) {
			throw new IllegalStateException("applicationName can not be empty");
		}
		parameters.put("application", getApplication().getName());
		parameters.put("application.version",
				!StringUtils.isEmpty(getApplication().getVersion()) ? getApplication().getVersion() : RpcConstant.DEFAULT_VERSION);
	}

	private void exportProtocol(Map<String, String> parameters) {
		parameters.put("protocol", !StringUtils.isEmpty(getProtocol().getName()) ? getProtocol().getName() : RpcConstant.DEFAULT_PROTOCOL);
		parameters.put("host", getProtocol().getHost());
		parameters.put("port", String.valueOf(getProtocol().getPort()));
		parameters.put("transporter",
				!StringUtils.isEmpty(getProtocol().getTransporter()) ? getProtocol().getTransporter() : RpcConstant.DEFAULT_TRANSPORTER);
		parameters.put("serialization",
				!StringUtils.isEmpty(getProtocol().getSerialization()) ? getProtocol().getSerialization() : RpcConstant.DEFAULT_SERIALIZATION);
		parameters.put("heartbeat", String.valueOf(getProtocol().getHeartbeat()));
	}

	private void exportRegistry(Map<String, String> parameters) {
		if (!StringUtils.isEmpty(getRegistry().getRegistryname())) {
			throw new IllegalStateException("registryname can not be empty");
		}
		if (!StringUtils.isEmpty(getRegistry().getAddress())) {
			throw new IllegalStateException("registryaddress can not be empty");
		}
		parameters.put("registryname", getRegistry().getRegistryname());
		parameters.put("address", getRegistry().getAddress());
		parameters.put("registry.timeout",
				getTimeout() > 0 ? String.valueOf(getRegistry().getTimeout()) : String.valueOf(RpcConstant.DEFAULT_REGISTRY_TIMEOUT));
		parameters.put("registry.sessiontimeout", getRegistry().getSessiontimeout() > 0 ?
				String.valueOf(getRegistry().getSessiontimeout()) :
				String.valueOf(RpcConstant.DEFAULT_REGISTRY_SESSIONTIMEOUT));
	}
}
