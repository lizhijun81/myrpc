package com.frameworkrpc.proxy;

import com.frameworkrpc.client.Client;
import com.frameworkrpc.client.ClientFactory;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.loadbalance.LoadBalance;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.registry.RegistrySide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AbstractProxy implements Serializable {

	private static final long serialVersionUID = 6592280954313884159L;
	private static final Logger logger = LoggerFactory.getLogger(AbstractProxy.class);
	protected final static Map<String, Client> connectedServerClients = new ConcurrentHashMap<>();
	protected LoadBalance loadBalance;
	protected Registry registry;

	protected void initProxy(URL url) {
		this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class)
				.getExtension(url.getParameter(RpcConstant.LOADBALANCE), Scope.SINGLETON);
		this.registry = ExtensionLoader.getExtensionLoader(RegistryFactory.class)
				.getExtension(url.getParameter(RpcConstant.REGISTRY_NAME), Scope.SINGLETON).getRegistry(url);
		this.registry.subscribe(url, RegistrySide.PROVIDER);
	}

	protected List<URL> getUrls(URL url) {
		List<URL> registryUrls = this.registry.discover(url, RegistrySide.PROVIDER).stream()
				.filter(f -> f.getParameter(RpcConstant.VERSION).equals(url.getParameter(RpcConstant.VERSION))).collect(Collectors.toList());

		if (registryUrls != null) {
			if (registryUrls.size() > 0) {
				Set<String> newAllServerNodeSet = new HashSet<String>();
				for (URL registryUrl : registryUrls) {
					newAllServerNodeSet.add(registryUrl.getServerPortStr());
				}

				// Add new server node
				for (String serverNodeAddress : newAllServerNodeSet) {
					if (!connectedServerClients.keySet().contains(serverNodeAddress)) {
						Client client = ExtensionLoader.getExtensionLoader(ClientFactory.class)
								.getExtension(url.getParameter(RpcConstant.TRANSPORTER), Scope.SINGLETON).getClient(loadBalance.select(getUrls(url)));
						if (!client.isOpened())
							client.doOpen();
						if (!client.isConnected())
							client.disConnect();
						connectedServerClients.put(serverNodeAddress, client);
					}
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
		Client client = connectedServerClients.get(loadBalance.select(getUrls(url)));
		if (!client.isOpened())
			client.doOpen();
		if (!client.isConnected())
			client.doConnect();
		return client;
	}

}
