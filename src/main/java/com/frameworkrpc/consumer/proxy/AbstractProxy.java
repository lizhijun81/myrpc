package com.frameworkrpc.consumer.proxy;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.consumer.cluster.ClusterInvoker;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class AbstractProxy implements ClassProxy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected URL url;
	protected ClusterInvoker clusterInvoker;

	public ClassProxy init() {
		this.clusterInvoker = ExtensionLoader.getExtensionLoader(ClusterInvoker.class).getExtension(url.getParameter(RpcConstants.CLUSTER_KEY));
		this.clusterInvoker.with(url).init();
		return this;
	}

	protected Object invoke(RpcRequest request, Class<?> returnType) {
		return clusterInvoker.invoke(request, returnType).get(url.getIntParameter(RpcConstants.TIMEOUT_KEY), TimeUnit.MILLISECONDS);
	}

}
