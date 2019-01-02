package com.myrpc.config;

import com.myrpc.common.RpcConstants;

import java.util.HashMap;
import java.util.Map;

public class ServiceConfig<T> extends ProviderConfig<T> {

	private static final long serialVersionUID = 4186914879813709242L;

	private void checkRef() {
		// reference should not be null, and is the implementation of the given interface
		if (ref == null) {
			throw new IllegalStateException("ref not allow null!");
		}
		if (!getInterfaceClass().isInstance(ref)) {
			throw new IllegalStateException("The class " + ref.getClass().getName() + " unimplemented interface " + getInterfaceClass() + "!");
		}
	}

	@Override
	public URL url() {

		checkRef();

		Map<String, String> parameters = new HashMap<>();
		addProviderParameters(parameters);
		addAppliactionParameters(parameters);
		addProtocolParameters(parameters);
		addRegistryParameters(parameters);
		String protocol = RpcConstants.PROVIDER;

		return new URL(protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface(), parameters);

	}
}
