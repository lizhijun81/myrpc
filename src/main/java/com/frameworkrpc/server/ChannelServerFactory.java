package com.frameworkrpc.server;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.netty.NettyRpcInstanceFatoryImpl;
import com.frameworkrpc.server.netty.NettyServer;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelServerFactory {

	private final static ConcurrentHashMap<String, ChannelServer> channelServerMap = new ConcurrentHashMap<>();

	public static ChannelServer createServer(URL url) {
		String key = String.format("%s", url.getServerPortStr());
		ChannelServerEnum serverEnum = ChannelServerEnum.getServerEnum(url.getParameter(RpcConstant.TRANSPORTER));
		if (!channelServerMap.containsKey(key)) {
			channelServerMap.put(key, createServer(url, serverEnum));
		}
		return channelServerMap.get(key);
	}

	private static ChannelServer createServer(URL url, ChannelServerEnum serverEnum) {
		switch (serverEnum) {
			case Netty:
				return new NettyServer(url, NettyRpcInstanceFatoryImpl.getInstance());
			default:
				return new NettyServer(url, NettyRpcInstanceFatoryImpl.getInstance());
		}
	}
}
