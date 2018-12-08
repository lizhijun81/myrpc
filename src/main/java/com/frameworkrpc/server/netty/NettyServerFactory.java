package com.frameworkrpc.server.netty;

import com.frameworkrpc.annotation.RpcComponent;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.enums.Scope;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.RpcInstanceFactory;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

@RpcComponent(name = "netty")
public class NettyServerFactory implements ServerFactory {
	@Override
	public Server getServer(URL url) {
		return new NettyServer(url, ExtensionLoader.getExtensionLoader(RpcInstanceFactory.class)
				.getExtension(url.getParameter(RpcConstant.TRANSPORTER), Scope.SINGLETON));
	}
}
