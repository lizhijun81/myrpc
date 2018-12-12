package com.frameworkrpc.proxy;

import com.frameworkrpc.client.Client;
import com.frameworkrpc.client.ClientFactory;
import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.loadbalance.LoadBalance;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractProxy implements Serializable {

	private static final long serialVersionUID = 6592280954313884159L;
	private static final Logger logger = LoggerFactory.getLogger(AbstractProxy.class);
	protected final Map<String, Client> connectedServerClients = new ConcurrentHashMap<>();
	protected LoadBalance loadBalance;
	protected Registry registry;
	protected RegistryListener registryListener;

	protected void initProxy(URL url) {
		this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(url.getParameter(RpcConstants.LOADBALANCE));

		this.registry = ExtensionLoader.getExtensionLoader(RegistryFactory.class)
				.getExtension(url.getParameter(RpcConstants.REGISTRY_NAME), Scope.SINGLETON).getRegistry(url);

		this.registryListener = new RegistryListener(url);
		URL subscribeUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
		this.registry.subscribe(subscribeUrl, registryListener);
		URL registryUrl = url.addParameters(RpcConstants.CATEGORY_KEY, RpcConstants.CONSUMERS_CATEGORY);
		this.registry.register(registryUrl);
	}

	protected List<URL> getUrls(URL url) {

		List<URL> registryUrls = registryListener.getInvokerUrls();

		if (registryUrls != null) {
			if (registryUrls.size() > 0) {
				for (URL registryUrl : registryUrls) {
					String serverNodeAddress = registryUrl.getServerPortStr();
					if (!connectedServerClients.keySet().contains(serverNodeAddress)) {
						Client client = ExtensionLoader.getExtensionLoader(ClientFactory.class)
								.getExtension(url.getParameter(RpcConstants.TRANSPORTER), Scope.SINGLETON)
								.getClient(registryUrl.addParameters(RpcConstants.CONNECTTIMEOUT, url.getParameter(RpcConstants.CONNECTTIMEOUT)));
						if (!client.isOpened())
							client.doOpen();
						if (!client.isConnected())
							client.doConnect();
						connectedServerClients.put(serverNodeAddress, client);
					}
				}
				Set<String> newAllServerNodeSet = new HashSet<String>();
				for (URL registryUrl : registryUrls) {
					newAllServerNodeSet.add(registryUrl.getServerPortStr());
				}
				for (Entry entry : connectedServerClients.entrySet()) {
					if (!newAllServerNodeSet.contains(entry.getKey())) {
						Client client = connectedServerClients.get(entry.getKey());
						if (client.isConnected())
							client.disConnect();
						connectedServerClients.remove(entry.getKey());
					}
				}
			} else {
				logger.error("No available server node. All server nodes are down !!!");
				for (Entry entry : connectedServerClients.entrySet()) {
					Client client = connectedServerClients.get(entry.getKey());
					if (client.isConnected())
						client.disConnect();
					connectedServerClients.remove(entry.getKey());
				}
				connectedServerClients.clear();
			}
		}
		return registryUrls;
	}

	protected Client getClient(URL url) {
		List<URL> urls = getUrls(url);
		String serverNodeAddress = loadBalance.select(urls).getServerPortStr();
		Client client = connectedServerClients.get(serverNodeAddress);
		if (!client.isOpened())
			client.doOpen();
		if (!client.isConnected())
			client.doConnect();
		return client;
	}

}
