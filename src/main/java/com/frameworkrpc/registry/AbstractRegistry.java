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
	protected final Map<String, ConcurrentHashMap<String, List<URL>>> subscribeUrls = new ConcurrentHashMap<String, ConcurrentHashMap<String, List<URL>>>() {
		{
			put(RegistrySide.PROVIDER.getName(), new ConcurrentHashMap<>());
			put(RegistrySide.CONSUMER.getName(), new ConcurrentHashMap<>());
		}
	};
	protected final Set<URL> registeredServiceUrls = newConcurrentHashSet();
	protected final Set<URL> registeredConsumerUrls = newConcurrentHashSet();


	protected void register(URL url, RegistrySide registrySide) {
		if (registrySide.getName().equals(registrySide.PROVIDER.getName()))
			registeredServiceUrls.add(url);
		else
			registeredConsumerUrls.add(url);
		logger.info("notify {} : {}", registrySide.getName(), url.toFullStr());
	}

	protected void unregister(URL url, RegistrySide registrySide) {
		if (registrySide.getName().equals(registrySide.PROVIDER.getName()))
			registeredServiceUrls.remove(url);
		else
			registeredConsumerUrls.remove(url);
		logger.info("remove {} : {}", registrySide.getName(), url.toFullStr());
	}

	public Set<URL> getRegisteredServiceUrls() {
		return this.registeredServiceUrls;
	}

}
