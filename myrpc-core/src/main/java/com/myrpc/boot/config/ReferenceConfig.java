package com.myrpc.boot.config;

import com.myrpc.common.RpcConstants;
import com.myrpc.consumer.proxy.ClassProxy;
import com.myrpc.extension.ExtensionLoader;

import java.util.HashMap;
import java.util.Map;

public class ReferenceConfig<T> extends ConsumerConfig<T> {

	private static final long serialVersionUID = 8866752725969090439L;

	private ClassProxy classProxy;

	public T get() {
		if (ref == null) {
			init();
		}
		return ref;
	}

	protected synchronized void init() {

		classProxy = ExtensionLoader.getExtension(ClassProxy.class, url().getParameter(RpcConstants.PROXY_KEY)).with(url()).init();
		ref = classProxy.newInstance(interfaceClass);
	}

	@Override
	public URL url() {

		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", getId());
		addServiceParameters(parameters);
		addConsumerParameters(parameters);
		addAppliactionParameters(parameters);
		addProtocolParameters(parameters);
		addRegistryParameters(parameters);
		String protocol = RpcConstants.CONSUMER;

		return new URL(protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface(), parameters);

	}
}
