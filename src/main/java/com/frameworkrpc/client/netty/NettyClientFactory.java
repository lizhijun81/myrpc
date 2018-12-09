package com.frameworkrpc.client.netty;

import com.frameworkrpc.client.Client;
import com.frameworkrpc.client.ClientFactory;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.URL;

@RpcComponent(name = "netty")
public class NettyClientFactory implements ClientFactory {

	@Override
	public Client getClient(URL url) {
		return new NettyClient(url);
	}
}
