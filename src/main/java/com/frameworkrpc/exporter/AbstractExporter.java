package com.frameworkrpc.exporter;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.Registry;
import com.frameworkrpc.registry.RegistryFactory;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Sets.newConcurrentHashSet;

public class AbstractExporter implements Serializable {

	private static final long serialVersionUID = -6374024642709901255L;
	protected final ConcurrentHashMap<String, Server> exportedServers = new ConcurrentHashMap<>();
	protected final Set<URL> exportedUrls = newConcurrentHashSet();
	protected URL url;
	protected Server server;
	protected Registry registry;

	public void setUrl(URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return this.url;
	}

	public void initExporter() {
		if (!exportedServers.containsKey(url.getServerPortStr())) {
			this.server = ExtensionLoader.getExtensionLoader(ServerFactory.class)
					.getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON).getServer(url);
			exportedServers.put(url.getServerPortStr(),this.server);
		}
		else {
			this.server = exportedServers.get(url.getServerPortStr());
		}

		this.registry = ExtensionLoader.getExtensionLoader(RegistryFactory.class)
				.getExtension(url.getParameter(RpcConstants.REGISTRY_NAME_KEY), Scope.SINGLETON).getRegistry(url);
	}
}
