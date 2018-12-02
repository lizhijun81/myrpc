package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.AbstractRegistry;
import com.frameworkrpc.registry.RegistryService;
import com.frameworkrpc.registry.ServiceListener;
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

public class ZookeeperRegistry extends AbstractRegistry implements RegistryService {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

	private ZkClient zkClient;

	private final ConcurrentHashMap<String, ConcurrentHashMap<ServiceListener, IZkChildListener>> subscribeListeners = new ConcurrentHashMap<>();

	private final ReentrantLock registryLock = new ReentrantLock();

	public ZookeeperRegistry(URL url) {
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
	}


	@Override
	public void registerService(URL url) {
		createNode(url, ZkNodeType.SERVER);
		registeredServiceUrls.add(url);
	}

	@Override
	public void unRegisterService(URL url) {
		removeNode(url, ZkNodeType.SERVER);
		registeredServiceUrls.remove(url);
	}

	private void createNode(URL url, ZkNodeType nodeType) {
		String nodeTypePath = ZKUtils.toNodeTypePath(url, nodeType);
		if (!zkClient.exists(nodeTypePath)) {
			zkClient.createPersistent(nodeTypePath, true);
		}
		zkClient.createEphemeral(ZKUtils.toNodePath(url, nodeType), url.toFullStr());
	}

	private void removeNode(URL url, ZkNodeType nodeType) {
		String nodePath = ZKUtils.toNodePath(url, nodeType);
		if (zkClient.exists(nodePath)) {
			zkClient.delete(nodePath);
		}
	}

	@Override
	public void registerConsumer(URL url) {
		createNode(url, ZkNodeType.CONSUMER);
		registerConsumersUrls.add(url);
	}

	@Override
	public void unRegisterConsumer(URL url) {
		removeNode(url, ZkNodeType.CONSUMER);
		registerConsumersUrls.remove(url);
	}

	private void reconnectService() {
		if (!registeredServiceUrls.isEmpty()) {
			try {
				registryLock.lock();
				for (URL url : registeredServiceUrls) {
					createNode(url, ZkNodeType.SERVER);
				}
			} finally {
				registryLock.unlock();
			}
		}
	}

	private void reconnectConsumer() {
		if (!registerConsumersUrls.isEmpty()) {
			try {
				registryLock.lock();
				for (URL url : registerConsumersUrls) {
					createNode(url, ZkNodeType.CONSUMER);
				}
			} finally {
				registryLock.unlock();
			}
		}
	}

	@Override
	public void subscribeService(URL url, ServiceListener serviceListener) {
		String parentPath = ZKUtils.toNodeTypePath(url, ZkNodeType.SERVER);
		if (subscribeListeners.containsKey(parentPath))
			return;
		try {
			registryLock.lock();
			IZkChildListener zkChildListener = new IZkChildListener() {
				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					serviceListener.handleServiceChanges(parentPath, currentChilds);
				}
			};
			zkClient.subscribeChildChanges(parentPath, zkChildListener);
			ConcurrentHashMap<ServiceListener, IZkChildListener> childListenerMap = new ConcurrentHashMap<>();
			childListenerMap.put(serviceListener, zkChildListener);
			subscribeListeners.put(parentPath, childListenerMap);
		} finally {
			registryLock.unlock();
		}
	}

	@Override
	public void unSubscribeService(URL url, ServiceListener serviceListener) {
		String parentPath = ZKUtils.toNodeTypePath(url, ZkNodeType.SERVER);
		if (!subscribeListeners.containsKey(parentPath))
			return;
		try {
			registryLock.lock();
			ConcurrentHashMap<ServiceListener, IZkChildListener> childListenerMap = subscribeListeners.get(parentPath);
			zkClient.unsubscribeChildChanges(parentPath, childListenerMap.get(serviceListener));
			subscribeListeners.remove(parentPath);
		} finally {
			registryLock.unlock();
		}
	}

	@Override
	public List<URL> discoverService(URL url) {
		String parentPath = ZKUtils.toNodeTypePath(url, ZkNodeType.SERVER);
		if (serviceDiscoveUrls.containsKey(parentPath)) {
			return serviceDiscoveUrls.get(parentPath);
		} else {
			List<String> currentChilds = new ArrayList<>();
			if (zkClient.exists(parentPath)) {
				currentChilds = zkClient.getChildren(parentPath);
			}
		}
		return null;
	}


}
