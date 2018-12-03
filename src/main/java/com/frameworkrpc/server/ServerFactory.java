package com.frameworkrpc.server;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.netty.NettyRpcInstanceFatoryImpl;
import com.frameworkrpc.server.netty.NettyServer;

import java.util.concurrent.ConcurrentHashMap;

public class ServerFactory {

	private final static ConcurrentHashMap<String, Server> serverMap = new ConcurrentHashMap<>();

	public static Server createServer(URL url) {
		String key = String.format("%s:%s", url.getServerPortStr(), url.getParameter(RpcConstant.TRANSPORTER));
		ServerEnum serverEnum = ServerEnum.getServerEnum(url.getParameter(RpcConstant.TRANSPORTER));
		if (!serverMap.containsKey(key)) {
			serverMap.put(key, createServer(url, serverEnum));
		}
		return serverMap.get(key);
	}

	private static Server createServer(URL url, ServerEnum serverEnum) {
		switch (serverEnum) {
			case Netty:
				return new NettyServer(url, NettyRpcInstanceFatoryImpl.getInstance());
			default:
				return new NettyServer(url, NettyRpcInstanceFatoryImpl.getInstance());
		}
	}
}
