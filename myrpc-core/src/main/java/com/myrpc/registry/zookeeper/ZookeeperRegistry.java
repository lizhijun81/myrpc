package com.myrpc.registry.zookeeper;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;
import com.myrpc.registry.AbstractRegistry;
import com.myrpc.registry.NotifyListener;
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

public class ZookeeperRegistry extends AbstractRegistry {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
	private ZkClient zkClient;
	private final Map<URL, ConcurrentMap<NotifyListener, IZkChildListener>> zkListeners = new ConcurrentHashMap<>();


	public ZookeeperRegistry (URL url) {

		super(url);

		zkClient = new ZkClient(url.getParameter(RpcConstants.REGISTRY_ADDRESS_KEY), url.getIntParameter(RpcConstants.REGISTRY_SESSIONTIMEOUT_KEY),
				url.getIntParameter(RpcConstants.REGISTRY_TIMEOUT_KEY));
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
		String nodePath = ZkUtils.toNodePath(url);
		if (!zkClient.exists(nodePath)) {
			zkClient.createEphemeral(nodePath);
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
					public void handleChildChange(String parentPath, List<String> currentChild) throws Exception {
						ZookeeperRegistry.this.notify(url, listener, toUrls(currentChild));
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

	@Override
	public void close() {
		this.zkClient.close();
	}

	private List<URL> toUrls(List<String> providers) {
		List<URL> urls = new ArrayList<>();
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
