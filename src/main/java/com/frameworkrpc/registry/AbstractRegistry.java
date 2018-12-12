package com.frameworkrpc.registry;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class AbstractRegistry {

	private static final Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);
	private final ConcurrentMap<URL, Map<String, List<URL>>> notified = new ConcurrentHashMap<URL, Map<String, List<URL>>>();
	private final ConcurrentMap<URL, Set<NotifyListener>> subscribed = new ConcurrentHashMap<URL, Set<NotifyListener>>();
	protected final Set<URL> registered = newConcurrentHashSet();

	protected void register(URL url) {
		registered.add(url);
		logger.info("notify {}", url.toFullStr());
	}

	protected void unregister(URL url) {
		registered.remove(url);
		logger.info("remove {}", url.toFullStr());
	}

	public void subscribe(URL url, NotifyListener listener) {
		if (url == null) {
			throw new IllegalArgumentException("subscribe url == null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("subscribe listener == null");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Subscribe: " + url);
		}
		Set<NotifyListener> listeners = subscribed.get(url);
		if (listeners == null) {
			subscribed.putIfAbsent(url, newConcurrentHashSet());
			listeners = subscribed.get(url);
		}
		listeners.add(listener);
	}

	public void unsubscribe(URL url, NotifyListener listener) {
		if (url == null) {
			throw new IllegalArgumentException("unsubscribe url == null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("unsubscribe listener == null");
		}
		if (logger.isInfoEnabled()) {
			logger.info("Unsubscribe: " + url);
		}
		Set<NotifyListener> listeners = subscribed.get(url);
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public Set<URL> getRegisteredUrls() {
		return this.registered;
	}


	protected void notify(URL url, NotifyListener listener, List<URL> urls) {
		if (url == null) {
			throw new IllegalArgumentException("notify url == null");
		}
		if (listener == null) {
			throw new IllegalArgumentException("notify listener == null");
		}
		if (urls == null || urls.isEmpty()) {
			logger.warn("Ignore empty notify urls for subscribe url " + url);
			return;
		}

		logger.info("Notify urls for subscribe url " + url + ", urls: " + urls);

		Map<String, List<URL>> result = new HashMap<String, List<URL>>();
		for (URL u : urls) {
			String category = u.getParameter(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
			List<URL> categoryList = result.get(category);
			if (categoryList == null) {
				categoryList = new ArrayList<URL>();
				result.put(category, categoryList);
			}
			categoryList.add(u);
		}
		if (result.size() == 0) {
			return;
		}
		Map<String, List<URL>> categoryNotified = notified.get(url);
		if (categoryNotified == null) {
			notified.putIfAbsent(url, new ConcurrentHashMap<String, List<URL>>());
			categoryNotified = notified.get(url);
		}
		for (Map.Entry<String, List<URL>> entry : result.entrySet()) {
			String category = entry.getKey();
			List<URL> categoryList = entry.getValue();
			categoryNotified.put(category, categoryList);
			listener.notify(categoryList);
		}
	}
}
