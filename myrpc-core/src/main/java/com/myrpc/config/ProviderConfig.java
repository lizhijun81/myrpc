package com.myrpc.config;

import com.myrpc.common.RpcConstants;
import com.myrpc.common.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class ProviderConfig<T> extends AbstractConfig {

	private static final long serialVersionUID = 5369377538761488535L;
	protected String interfaceName;
	protected Class<T> interfaceClass;
	protected volatile T ref;
	protected String version;
	protected String group;
	protected int timeout;
	protected ApplicationConfig application;
	protected RegistryConfig registry;
	protected ProtocolConfig protocol;

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

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ApplicationConfig getApplication() {
		return application;
	}

	public void setApplication(ApplicationConfig application) {
		this.application = application;
	}

	public RegistryConfig getRegistry() {
		return registry;
	}

	public void setRegistry(RegistryConfig registry) {
		this.registry = registry;
	}

	public ProtocolConfig getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolConfig protocol) {
		this.protocol = protocol;
	}

	public abstract URL url();

	public URL getAppliactionUrl() {
		Map<String, String> parameters = new HashMap<>();
		addAppliactionParameters(parameters);
		String protocol = RpcConstants.APPLICATION;
		return new URL(protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), "AppliactionConfig", parameters);
	}

	public URL getRegistryUrl() {
		Map<String, String> parameters = new HashMap<>();
		addAppliactionParameters(parameters);
		addRegistryParameters(parameters);
		String protocol = RpcConstants.REGISTRY;
		return new URL(protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), "RegistryService", parameters);
	}

	public URL getProtocolUrl() {
		Map<String, String> parameters = new HashMap<>();
		addAppliactionParameters(parameters);
		addProtocolParameters(parameters);
		String protocol = RpcConstants.PROTOCOL;
		return new URL(protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), "ProtocolConfig", parameters);
	}

	protected void addProviderParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getInterface())) {
			throw new IllegalStateException("interfaceName can not be empty");
		}
		parameters.put(RpcConstants.INTERFACE_KEY, getInterface());
		parameters.put(RpcConstants.VERSION_KEY, getVal(getVersion(), RpcConstants.DEFAULT_VERSION));
		parameters.put(RpcConstants.GROUP_KEY, getVal(getGroup(), RpcConstants.DEFAULT_GROUP));
		parameters.put(RpcConstants.TIMEOUT_KEY, getVal(getTimeout(), RpcConstants.DEFAULT_TMEOUT));
	}

	protected void addAppliactionParameters(Map<String, String> parameters) {
		getApplication().addAppliactionParameters(parameters);
	}

	protected void addRegistryParameters(Map<String, String> parameters) {
		getRegistry().addRegistryParameters(parameters);
	}

	protected void addProtocolParameters(Map<String, String> parameters) {
		if (this.protocol == null)
			this.protocol = new ProtocolConfig();
		getProtocol().addProtocolParameters(parameters);

	}


}
