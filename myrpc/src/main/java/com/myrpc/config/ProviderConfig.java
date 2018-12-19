package com.myrpc.config;

import com.myrpc.common.RpcConstants;
import com.myrpc.common.StringUtils;

import java.util.Map;

public class ProviderConfig<T> extends AbstractConfig {

	private static final long serialVersionUID = 5369377538761488535L;
	protected String interfaceName;
	protected Class<T> interfaceClass;
	protected volatile T ref;
	protected ApplicationConfig application;
	protected RegistryConfig registry;
	protected ProtocolConfig protocol;
	protected String version;
	protected String group;
	protected int timeout;

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


	protected void addServiceParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getInterface())) {
			throw new IllegalStateException("interfaceName can not be empty");
		}
		parameters.put(RpcConstants.INTERFACE_KEY, getInterface());
		parameters.put(RpcConstants.VERSION_KEY, getVal(getVersion(), RpcConstants.DEFAULT_VERSION));
		parameters.put(RpcConstants.GROUP_KEY, getVal(getGroup(), RpcConstants.DEFAULT_GROUP));
		parameters.put(RpcConstants.TIMEOUT_KEY, getVal(getTimeout(), RpcConstants.DEFAULT_TMEOUT));
	}


	protected void addAppliactionParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getApplication().getName())) {
			throw new IllegalStateException("applicationName can not be empty");
		}
		parameters.put(RpcConstants.APPLICATION_KEY, getApplication().getName());
		parameters.put(RpcConstants.APPLICATION_VERSION_KEY, getVal(getApplication().getVersion(), RpcConstants.DEFAULT_VERSION));
	}

	protected void addProtocolParameters(Map<String, String> parameters) {
		if (this.protocol == null)
			this.protocol = new ProtocolConfig();
		parameters.put(RpcConstants.PROTOCOL_KEY, getVal(getProtocol().getName(), RpcConstants.DEFAULT_PROTOCOL));
		parameters.put(RpcConstants.HOST_KEY, getProtocol().getHost());
		parameters.put(RpcConstants.PORT_KEY, String.valueOf(getProtocol().getPort()));
		parameters.put(RpcConstants.TRANSPORTER_KEY, getVal(getProtocol().getTransporter(), RpcConstants.DEFAULT_TRANSPORTER));
		parameters.put(RpcConstants.SERIALIZATION_KEY, getVal(getProtocol().getSerialization(), RpcConstants.DEFAULT_SERIALIZATION));
		parameters.put(RpcConstants.HEARTBEAT_KEY, getVal(getProtocol().getHeartbeat(), RpcConstants.DEFAULT_HEARTBEAT));
		parameters.put(RpcConstants.THREADPOOL_KEY, getVal(getProtocol().getThreadpool(), RpcConstants.DEFAULT_THREADPOOL));
		parameters.put(RpcConstants.THREADS_KEY, getVal(getProtocol().getThreads(), RpcConstants.DEFAULT_THREADS));
		parameters.put(RpcConstants.IOTHREADS_KEY, getVal(getProtocol().getIothreads(), RpcConstants.DEFAULT_IOTHREADS));
	}

	protected void addRegistryParameters(Map<String, String> parameters) {
		if (StringUtils.isEmpty(getRegistry().getName())) {
			throw new IllegalStateException("registry.name can not be empty");
		}
		if (StringUtils.isEmpty(getRegistry().getAddress())) {
			throw new IllegalStateException("registry.address can not be empty");
		}
		parameters.put(RpcConstants.REGISTRY_NAME_KEY, getRegistry().getName());
		parameters.put(RpcConstants.REGISTRY_ADDRESS_KEY, getRegistry().getAddress());
		parameters.put(RpcConstants.REGISTRY_TIMEOUT_KEY, getVal(getRegistry().getTimeout(), RpcConstants.DEFAULT_REGISTRY_TIMEOUT));
		parameters.put(RpcConstants.REGISTRY_SESSIONTIMEOUT_KEY,
				getVal(getRegistry().getSessiontimeout(), RpcConstants.DEFAULT_REGISTRY_SESSIONTIMEOUT));
	}
}
