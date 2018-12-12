package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.AbstractRegistry;
import com.frameworkrpc.registry.NotifyListener;
import com.frameworkrpc.registry.Registry;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ZookeeperRegistry extends AbstractRegistry implements Registry {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
	private ZkClient zkClient;
	private final Map<URL, ConcurrentMap<NotifyListener, IZkChildListener>> zkListeners = new ConcurrentHashMap<>();

	public ZookeeperRegistry(URL url) {
		zkClient = new ZkClient(url.getParameter(RpcConstants.ADDRESS), url.getIntParameter(RpcConstants.REGISTRY_SESSIONTIMEOUT),
				url.getIntParameter(RpcConstants.REGISTRY_TIMEOUT));
		IZkStateListener zkStateListener = new IZkStateListener() {
			@Override
			public void handleStateChanged(Watcher.Event.KeeperState state) {
			}

			@Override
			public void handleNewSession() {
				logger.info("zkRegistry get new session notify.");
				reconnect();
			}

			@Override
			public void handleSessionEstablishmentError(Throwable throwable) {
			}
		};
		zkClient.subscribeStateChanges(zkStateListener);
	}

	@Override
	public void register(URL url) {
		createNode(url);
		super.register(url);
	}

	@Override
	public void unregister(URL url) {
		removeNode(url);
		super.unregister(url);
	}

	private void createNode(URL url) {
		String nodeTypePath = ZkUtils.toCategoryPath(url);
		if (!zkClient.exists(nodeTypePath)) {
			zkClient.createPersistent(nodeTypePath, true);
		}
		String nodePrth = ZkUtils.toNodePath(url);
		if (!zkClient.exists(nodePrth)) {
			zkClient.createEphemeral(nodePrth);
		}
	}

	private void removeNode(URL url) {
		String nodePath = ZkUtils.toNodePath(url);
		if (zkClient.exists(nodePath)) {
			zkClient.delete(nodePath);
		}
	}


	private void reconnect() {
		if (!registered.isEmpty()) {
			synchronized (this) {
				for (URL url : registered) {
					createNode(url);
					logger.warn("reconnect: {}", url.toFullStr());
				}
			}
		}
	}


	@Override
	public void subscribe(final URL url, final NotifyListener listener) {
		List<URL> urls = new ArrayList<>();
		for (String path : ZkUtils.toCategoriesPath(url)) {
			ConcurrentMap<NotifyListener, IZkChildListener> listeners = zkListeners.get(url);
			if (listeners == null) {
				zkListeners.putIfAbsent(url, new ConcurrentHashMap<>());
				listeners = zkListeners.get(url);
			}
			IZkChildListener zkListener = listeners.get(listener);
			if (zkListener == null) {
				listeners.putIfAbsent(listener, new IZkChildListener() {
					@Override
					public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
						ZookeeperRegistry.this.notify(url, listener, toUrls(currentChilds));
					}

				});
				zkListener = listeners.get(listener);
			}
			List<String> children = zkClient.subscribeChildChanges(path, zkListener);
			if (children != null) {
				urls.addAll(toUrls(children));
			}
		}
		notify(url, listener, urls);
	}


	@Override
	public void unsubscribe(URL url, NotifyListener listener) {
		ConcurrentMap<NotifyListener, IZkChildListener> listeners = zkListeners.get(url);
		if (listeners != null) {
			IZkChildListener zkListener = listeners.get(listener);
			if (zkListener != null) {
				String path = ZkUtils.toCategoryPath(url);
				zkClient.unsubscribeChildChanges(path, zkListener);
			}
		}
	}

	private List<URL> toUrls(List<String> providers) {
		List<URL> urls = new ArrayList<URL>();
		if (providers != null && !providers.isEmpty()) {
			for (String provider : providers) {
				provider = URL.decode(provider);
				URL url = new URL(provider);
				urls.add(url);
			}
		}
		return urls;
	}

}
