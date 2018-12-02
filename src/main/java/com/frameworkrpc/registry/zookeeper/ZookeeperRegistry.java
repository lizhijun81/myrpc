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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ZookeeperRegistry extends AbstractRegistry implements RegistryService {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

	private ZkClient zkClient;

	private ServiceListener serviceListener;

	private final ConcurrentHashMap<String, IZkChildListener> subscribeListeners = new ConcurrentHashMap<>();

	private final ReentrantLock registryLock = new ReentrantLock();

	public ZookeeperRegistry(URL url, ServiceListener serviceListener) {
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
		this.serviceListener = serviceListener;
	}


	@Override
	public void registerService(URL url) {
		createNode(url, ZkNodeType.PROVIDER);
		registeredServiceUrls.add(url);
	}

	@Override
	public void unRegisterService(URL url) {
		removeNode(url, ZkNodeType.PROVIDER);
		registeredServiceUrls.remove(url);
	}

	private void createNode(URL url, ZkNodeType nodeType) {
		String nodeTypePath = ZkUtils.toNodeTypePath(url, nodeType);
		if (!zkClient.exists(nodeTypePath)) {
			zkClient.createPersistent(nodeTypePath, true);
		}
		zkClient.createEphemeral(ZkUtils.toNodePath(url, nodeType), url.toFullStr());
	}

	private void removeNode(URL url, ZkNodeType nodeType) {
		String nodePath = ZkUtils.toNodePath(url, nodeType);
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
					createNode(url, ZkNodeType.PROVIDER);
				}
				logger.warn("reconnectService {}", registeredServiceUrls);
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
				logger.warn("reconnectConsumer {}", registerConsumersUrls);
			} finally {
				registryLock.unlock();
			}
		}
	}

	@Override
	public void subscribeService(URL url) {
		String parentPath = ZkUtils.toNodeTypePath(url, ZkNodeType.PROVIDER);
		if (subscribeListeners.containsKey(parentPath))
			return;
		try {
			registryLock.lock();
			IZkChildListener zkChildListener = new IZkChildListener() {
				@Override
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					serviceListener.handleNodeChildChange(parentPath, currentChilds, serviceDiscoveUrls);
				}
			};
			zkClient.subscribeChildChanges(parentPath, zkChildListener);
			subscribeListeners.put(parentPath, zkChildListener);
		} finally {
			registryLock.unlock();
		}
	}

	@Override
	public void unSubscribeService(URL url) {
		String parentPath = ZkUtils.toNodeTypePath(url, ZkNodeType.PROVIDER);
		if (!subscribeListeners.containsKey(parentPath))
			return;
		try {
			registryLock.lock();
			IZkChildListener zkChildListener = subscribeListeners.get(parentPath);
			zkClient.unsubscribeChildChanges(parentPath, zkChildListener);
			subscribeListeners.remove(parentPath);
		} finally {
			registryLock.unlock();
		}
	}

	@Override
	public List<URL> discoverService(URL url) {
		String parentPath = ZkUtils.toNodeTypePath(url, ZkNodeType.PROVIDER);
		if (serviceDiscoveUrls.containsKey(parentPath))
			return serviceDiscoveUrls.get(parentPath);
		if (zkClient.exists(parentPath)) {
			List<String> currentChilds = zkClient.getChildren(parentPath);
			serviceListener.handleNodeChildChange(parentPath, currentChilds, serviceDiscoveUrls);
		}
		return serviceDiscoveUrls.get(parentPath);
	}
}
