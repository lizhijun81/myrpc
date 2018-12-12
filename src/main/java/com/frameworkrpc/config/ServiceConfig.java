package com.frameworkrpc.config;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.exporter.Exporter;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.rpc.RpcInstanceFactory;

public class ServiceConfig<T> extends ExporterConfig {

	private static final long serialVersionUID = 4186914879813709242L;
    private volatile boolean isexport;

	public void export() {
		if (!isexport){
			synchronized (this){
				if (!isexport) {
					checkRef();
					RpcInstanceFactory rpcInstanceFactory = ExtensionLoader.getExtensionLoader(RpcInstanceFactory.class)
							.getExtension(getUrl().getParameter(RpcConstants.TRANSPORTER), Scope.SINGLETON);
					rpcInstanceFactory.setRpcInstance(getInterface(), getRef());
					exporter = ExtensionLoader.getExtensionLoader(Exporter.class).getExtension("default");
					exporter.setUrl(getUrl());
					exporter.initExporter();
					exporter.exportServer();
					exporter.exportUrl();
				}
			}
		}
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
