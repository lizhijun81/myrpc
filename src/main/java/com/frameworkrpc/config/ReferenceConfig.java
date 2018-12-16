package com.frameworkrpc.config;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.consumer.proxy.ClassProxy;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.model.URL;

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
		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", getId());
		addServiceParameters(parameters);
		addConsumerParameters(parameters);
		addAppliactionParameters(parameters);
		addProtocolParameters(parameters);
		addRegistryParameters(parameters);
		String protocol = RpcConstants.CONSUMER;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append(String.format("%s://%s:%s/%s?", protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface()));
		stringBuilder.append(NetUtils.getUrlParamsByMap(parameters));

		URL url = new URL(stringBuilder.toString());

		classProxy = ExtensionLoader.getExtensionLoader(ClassProxy.class).getExtension(url.getParameter(RpcConstants.PROXY_KEY)).with(url).init();
		ref = classProxy.getInstance(interfaceClass);
	}



}
