package com.frameworkrpc.registry;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class AbstractRegistry {

	private static final Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);
	protected final ConcurrentHashMap<String, List<URL>> serviceDiscoveUrls = new ConcurrentHashMap<>();
	protected final Set<URL> registeredServiceUrls = newConcurrentHashSet();
	protected final Set<URL> registerConsumersUrls = newConcurrentHashSet();

	protected void registerService(URL url) {
		registeredServiceUrls.add(url);
		logger.info("notify service : {}", url.toFullStr());
	}

	protected void unRegisterService(URL url) {
		registeredServiceUrls.remove(url);
		logger.info("remove service : {}", url.toFullStr());
	}

	protected void registerConsumer(URL url) {
		registerConsumersUrls.add(url);
		logger.info("notify consumer : {}", url.toFullStr());
	}

	protected void unRegisterConsumer(URL url) {
		registerConsumersUrls.remove(url);
		logger.info("remove consumer : {}", url.toFullStr());
	}

	protected List<URL> discoverService(URL url, List<URL> discoveUrls) {
		return discoveUrls.stream().filter(f -> {
			return url.getParameter(RpcConstant.VERSION).equals(f.getParameter(RpcConstant.VERSION))
					&& url.getParameter(RpcConstant.TRANSPORTER).equals(f.getParameter(RpcConstant.TRANSPORTER));
		}).collect(Collectors.toList());
	}

}
