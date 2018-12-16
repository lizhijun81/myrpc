package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.RpcInstanceFactory;
import com.frameworkrpc.server.exporter.Exporter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class ServiceConfig<T> extends ProviderConfig<T> {

	private static final long serialVersionUID = 4186914879813709242L;

	public static final Set<Exporter> exporters = newConcurrentHashSet();

	public synchronized void export() {
		checkRef();

		Map<String, String> parameters = new HashMap<>();
		parameters.put("id", getId());
		addServiceParameters(parameters);
		addAppliactionParameters(parameters);
		addProtocolParameters(parameters);
		addRegistryParameters(parameters);
		String protocol = RpcConstants.PROVIDER;

		URL url = new URL(protocol, getProtocol().getHost(), String.valueOf(getProtocol().getPort()), getInterface(), parameters);


		exporters.add(new Exporter().with(url).init().start().publish());

		RpcInstanceFactory rpcInstanceFactory = ExtensionLoader.getExtensionLoader(RpcInstanceFactory.class)
				.getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON);
		rpcInstanceFactory.setRpcInstance(getInterface(), getRef());
	}


	private void checkRef() {
		// reference should not be null, and is the implementation of the given interface
		if (ref == null) {
			throw new IllegalStateException("ref not allow null!");
		}
		if (!getInterfaceClass().isInstance(ref)) {
			throw new IllegalStateException("The class " + ref.getClass().getName() + " unimplemented interface " + getInterfaceClass() + "!");
		}
	}

}
