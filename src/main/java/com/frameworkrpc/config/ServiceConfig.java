package com.frameworkrpc.config;

import com.frameworkrpc.exporter.RpcExporter;
import com.frameworkrpc.rpc.netty.NettyRpcInstanceFatoryImpl;

public class ServiceConfig<T> extends SerRefConfig {

	private static final long serialVersionUID = 4186914879813709242L;
	private T ref;


	public T getRef() {
		return ref;
	}

	public void setRef(T ref) {
		this.ref = ref;
	}

	public void export() {
		checkRef();
		exporter = new RpcExporter(getURL()).export();
		NettyRpcInstanceFatoryImpl.getInstance().setRpcInstance(getInterface(), getRef());
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
