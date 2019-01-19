package com.myrpc.consumer.proxy;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;
import com.myrpc.consumer.cluster.HaStrategy;
import com.myrpc.consumer.cluster.LoadBalance;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.model.RpcRequest;
import com.myrpc.registry.Registry;
import com.myrpc.registry.RegistryFactory;
import com.myrpc.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractProxy implements ClassProxy {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected URL url;
	protected Registry registry;
	protected RegistryListener registryListener;
	protected LoadBalance loadBalance;
	protected HaStrategy clusterInvoker;

	@Override
	public ClassProxy with(URL url) {
		this.url = url;
		return this;
	}

	@Override
	public ClassProxy init() {


		this.registry = ExtensionLoader.getExtension(RegistryFactory.class, url.getParameter(RpcConstants.REGISTRY_NAME_KEY)).getRegistry(url);

		this.registryListener = new RegistryListener(url);
		URL subscribeUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		this.registry.subscribe(subscribeUrl, registryListener);

		URL providerUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.CONSUMERS_CATEGORY);
		this.registry.register(providerUrl);

		this.loadBalance = ExtensionLoader.getExtension(LoadBalance.class, url.getParameter(RpcConstants.LOADBALANCE_KEY)).with(registryListener);

		this.clusterInvoker = ExtensionLoader.getExtension(HaStrategy.class, url.getParameter(RpcConstants.CLUSTER_KEY)).with(url, loadBalance)
				.init();

		return this;
	}

	protected Object invoke(RpcRequest request, Class<?> returnType) {
		try {

			URL providerUrl = loadBalance.getProviderUrl();

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
