package com.myrpc.client.netty;

import com.myrpc.client.Client;
import com.myrpc.client.ClientFactory;
import com.myrpc.extension.RpcComponent;
import com.myrpc.model.URL;

@RpcComponent(name = "netty")
public class NettyClientFactory implements ClientFactory {

	@Override
	public Client getClient(URL url) {
		return new NettyClient(url);
	}
}
