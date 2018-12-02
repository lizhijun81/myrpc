package com.frameworkrpc.server;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.server.netty.NettyServer;

import java.util.concurrent.ConcurrentHashMap;

public class ServerFactory {

	private final static ConcurrentHashMap<String, Server> serverMap = new ConcurrentHashMap<>();

	public static Server createServer(URL url) {
		ServerEnum serverEnum = ServerEnum.getServerEnum(url.getParameter(RpcConstant.SERVER));
		if (!serverMap.containsKey(url.toFullStr())) {
			serverMap.put(url.toFullStr(), createServer(url, serverEnum));
		}
		return serverMap.get(url.toFullStr());
	}

	private static Server createServer(URL url, ServerEnum serverEnum) {
		switch (serverEnum) {
			case Netty:
				return new NettyServer(url);
			default:
				return new NettyServer(url);
		}
	}
}
