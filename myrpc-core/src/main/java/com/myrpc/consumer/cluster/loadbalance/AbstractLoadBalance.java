package com.myrpc.consumer.cluster.loadbalance;

import com.myrpc.config.URL;
import com.myrpc.consumer.cluster.LoadBalance;
import com.myrpc.exception.MyRpcInvokeException;
import com.myrpc.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newConcurrentHashSet;

abstract class AbstractLoadBalance implements LoadBalance {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected final Set<URL> providerUrls = newConcurrentHashSet();

	protected RegistryListener registryListener;

	public LoadBalance with(RegistryListener registryListener) {

		this.registryListener = registryListener;
		return this;
	}

	protected List<URL> getProviderUrls() {
		List<URL> _providerUrls = registryListener.getProviderUrls();

		if ((_providerUrls == null || _providerUrls.isEmpty()) && (providerUrls == null || providerUrls.isEmpty())) {
			throw new MyRpcInvokeException("providerUrl can not be empty");
		}

		if (_providerUrls.size() > 0) {

			providerUrls.clear();

			for (URL providerUrl : _providerUrls) {
				if (!providerUrls.contains(providerUrl)) {
					providerUrls.add(providerUrl);
				}
			}

		} else {

			logger.error("No available server node. All server nodes are down !!!");
			providerUrls.clear();
		}

		return new ArrayList<>(providerUrls);
	}

	public URL getProviderUrl() {

		List<URL> _providerUrls = getProviderUrls();
		URL providerUrl = select(_providerUrls);
		return providerUrl;
	}
}
