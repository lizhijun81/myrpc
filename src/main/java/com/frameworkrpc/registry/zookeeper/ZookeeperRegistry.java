package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.AbstractRegistry;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryListener;
import com.frameworkrpc.registry.RegistrySide;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ZookeeperRegistry extends AbstractRegistry implements Registry {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
	private ZkClient zkClient;
	private RegistryListener registryListener;
	private final ConcurrentHashMap<String, IZkChildListener> subscribeListeners = new ConcurrentHashMap<>();
	private final ReentrantLock providerLock = new ReentrantLock();
	private final ReentrantLock consumerLock = new ReentrantLock();

	public ZookeeperRegistry(URL url, RegistryListener registryListener) {
		zkClient = new ZkClient(url.getParameter(RpcConstant.ADDRESS), url.getIntParameter(RpcConstant.REGISTRY_SESSIONTIMEOUT),
				url.getIntParameter(RpcConstant.REGISTRY_TIMEOUT));
		IZkStateListener zkStateListener = new IZkStateListener() {
			@Override
			public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
				// do nothing
			}

			@Override
			public void handleNewSession() throws Exception {
				logger.info("zkRegistry get new session notify.");
				reconnectService();
				reconnectConsumer();
			}

			@Override
			public void handleSessionEstablishmentError(Throwable throwable) throws Exception {

			}
		};
		zkClient.subscribeStateChanges(zkStateListener);
		this.registryListener = registryListener;
	}


	@Override
	public void register(URL url, RegistrySide registrySide) {
		createNode(url, registrySide);
		super.register(url, registrySide);
	}

	@Override
	public void unregister(URL url, RegistrySide registrySide) {
		removeNode(url, registrySide);
		super.unregister(url, registrySide);
	}

	private void createNode(URL url, RegistrySide registrySide) {
		String nodeTypePath = ZkUtils.toRegistryPath(url, registrySide);
		if (!zkClient.exists(nodeTypePath)) {
			zkClient.createPersistent(nodeTypePath, true);
		}
		String nodePrth = ZkUtils.toNodePath(url, registrySide);
		if (!zkClient.exists(nodePrth)) {
			zkClient.createEphemeral(nodePrth);
		}
	}

	private void removeNode(URL url, RegistrySide registrySide) {
		String nodePath = ZkUtils.toNodePath(url, registrySide);
		if (zkClient.exists(nodePath)) {
			zkClient.delete(nodePath);
		}
	}


	private void reconnectService() {
		if (!registeredProviderUrls.isEmpty()) {
			try {
				providerLock.lock();
				for (URL url : registeredProviderUrls) {
					createNode(url, RegistrySide.PROVIDER);
				}
				logger.warn("reconnectService: {}", registeredProviderUrls);
			} finally {
				providerLock.unlock();
			}
		}
	}

	private void reconnectConsumer() {
		if (!registerConsumerUrls.isEmpty()) {
			try {
				consumerLock.lock();
				for (URL url : registerConsumerUrls) {
					createNode(url, RegistrySide.CONSUMER);
				}
				logger.warn("reconnectConsumer: {}", registerConsumerUrls);
			} finally {
				consumerLock.unlock();
			}
		}
	}

	@Override
	public void subscribe(URL url, RegistrySide registrySide) {
		String parentPath = ZkUtils.toRegistryPath(url, registrySide);
		if (subscribeListeners.containsKey(parentPath))
			return;
		try {
			consumerLock.lock();
			if (subscribeListeners.containsKey(parentPath))
				return;
			IZkChildListener zkChildListener = new IZkChildListener() {
				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					List<URL> registryUrls = currentChilds.stream().map(url -> {
						return new URL(url);
					}).collect(Collectors.toList());
					logger.info("subscribe:{} registryUrls:{}", parentPath, registryUrls);
					List<URL> urls = subscribeUrls.getOrDefault(registrySide.getName(), new ConcurrentHashMap<>())
							.getOrDefault(parentPath, new ArrayList<>());
					registryListener.notifyRegistryUrl(registryUrls, urls);
				}
			};
			zkClient.subscribeChildChanges(parentPath, zkChildListener);
			subscribeListeners.put(parentPath, zkChildListener);
			logger.info("subscribeService: {}", url.toFullStr());
		} finally {
			consumerLock.unlock();
		}
	}

	@Override
	public void unsubscribe(URL url, RegistrySide registrySide) {
		String parentPath = ZkUtils.toRegistryPath(url, registrySide);
		if (!subscribeListeners.containsKey(parentPath))
			return;
		try {
			consumerLock.lock();
			if (!subscribeListeners.containsKey(parentPath))
				return;
			IZkChildListener zkChildListener = subscribeListeners.get(parentPath);
			zkClient.unsubscribeChildChanges(parentPath, zkChildListener);
			subscribeListeners.remove(parentPath);
			logger.info("unSubscribeService: {}", url.toFullStr());
		} finally {
			consumerLock.unlock();
		}
	}

	@Override
	public List<URL> discover(URL url, RegistrySide registrySide) {
		String parentPath = ZkUtils.toRegistryPath(url, registrySide);
		if (subscribeUrls.containsKey(registrySide.getName()) && subscribeUrls.get(registrySide.getName()).containsKey(parentPath))
			return subscribeUrls.get(registrySide.getName()).get(parentPath);
		if (zkClient.exists(parentPath)) {
			List<String> currentChilds = zkClient.getChildren(parentPath);
			List<URL> registryUrls = currentChilds.stream().map(url1 -> {
				return new URL(url1);
			}).collect(Collectors.toList());
			List<URL> urls = subscribeUrls.getOrDefault(registrySide.getName(), new ConcurrentHashMap<>())
					.getOrDefault(parentPath, new ArrayList<>());
			registryListener.notifyRegistryUrl(registryUrls, urls);
		}
		return subscribeUrls.get(registrySide.getName()).get(parentPath);
	}
}
