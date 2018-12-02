package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class AbstractRegistry {
	protected final ConcurrentHashMap<String, List<URL>> serviceDiscoveUrls = new ConcurrentHashMap<>();
	protected final Set<URL> registeredServiceUrls = newConcurrentHashSet();
	protected final Set<URL> registerConsumersUrls = newConcurrentHashSet();
}
