package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class AbstractRegistry {

	private static final Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);
	protected final Map<String, ConcurrentHashMap<String, List<URL>>> subscribeUrls = new ConcurrentHashMap<>();
	protected final Set<URL> providerUrls = newConcurrentHashSet();
	protected final Set<URL> consumerUrls = newConcurrentHashSet();

	protected void register(URL url, RegistrySide registrySide) {
		if (registrySide.getName().equals(registrySide.PROVIDER.getName()))
			providerUrls.add(url);
		else
			providerUrls.add(url);
		logger.info("notify {} : {}", registrySide.getName(), url.toFullStr());
	}

	protected void unregister(URL url, RegistrySide registrySide) {
		if (registrySide.getName().equals(registrySide.PROVIDER.getName()))
			consumerUrls.remove(url);
		else
			consumerUrls.remove(url);
		logger.info("remove {} : {}", registrySide.getName(), url.toFullStr());
	}

}
