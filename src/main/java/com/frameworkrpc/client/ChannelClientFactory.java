package com.frameworkrpc.client;

import com.frameworkrpc.client.netty.NettyClient;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelClientFactory {

	private final static ConcurrentHashMap<String, ChannelClient> channelClientMap = new ConcurrentHashMap<>();

	public static ChannelClient createClient(URL url) {
		String key = String.format("%s", url.getServerPortStr());
		ChannelClientEnum clientEnum = ChannelClientEnum.getClientEnumm(url.getParameter(RpcConstant.TRANSPORTER));
		if (!channelClientMap.containsKey(key)) {
			channelClientMap.put(key, createClient(url, clientEnum));
		}
		return channelClientMap.get(key);
	}

	private static ChannelClient createClient(URL url, ChannelClientEnum clientEnum) {
		switch (clientEnum) {
			case Netty:
				return new NettyClient(url);
			default:
				return new NettyClient(url);
		}
	}
}
