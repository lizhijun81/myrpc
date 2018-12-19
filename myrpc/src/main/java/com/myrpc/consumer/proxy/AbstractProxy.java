package com.myrpc.consumer.proxy;

import com.myrpc.common.RpcConstants;
import com.myrpc.consumer.cluster.ClusterInvoker;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractProxy implements ClassProxy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected URL url;
	protected ClusterInvoker clusterInvoker;

	@Override
	public ClassProxy with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public ClassProxy init() {
		this.clusterInvoker = ExtensionLoader.getExtensionLoader(ClusterInvoker.class).getExtension(url.getParameter(RpcConstants.CLUSTER_KEY));
		this.clusterInvoker.with(url).init();
		return this;
	}

	protected Object invoke(RpcRequest request, Class<?> returnType) {
		try {
			return clusterInvoker.invoke(request, returnType).get(url.getIntParameter(RpcConstants.TIMEOUT_KEY), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("invoke failed", e);
		} catch (ExecutionException e) {
			logger.error("invoke failed", e);
		} catch (TimeoutException e) {
			logger.error("invoke failed", e);
		}
		return null;
	}

}
