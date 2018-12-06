package com.frameworkrpc.server;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.netty.NettyRpcInstanceFatoryImpl;
import com.frameworkrpc.server.netty.NettyServer;

import java.util.concurrent.ConcurrentHashMap;

public class ServerFactory {

	private final static ConcurrentHashMap<String, ChannelServer> serverMap = new ConcurrentHashMap<>();

	public static ChannelServer createServer(URL url) {
		String key = String.format("%s", url.getServerPortStr());
		ServerEnum serverEnum = ServerEnum.getServerEnum(url.getParameter(RpcConstant.TRANSPORTER));
		if (!serverMap.containsKey(key)) {
			serverMap.put(key, createServer(url, serverEnum));
		}
		return serverMap.get(key);
	}

	private static ChannelServer createServer(URL url, ServerEnum serverEnum) {
		switch (serverEnum) {
			case Netty:
				return new NettyServer(url, NettyRpcInstanceFatoryImpl.getInstance());
			default:
				return new NettyServer(url, NettyRpcInstanceFatoryImpl.getInstance());
		}
	}
}
