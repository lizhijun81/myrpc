package com.frameworkrpc.server.netty;

import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.RpcInstanceFactory;
import com.frameworkrpc.server.Server;
import com.frameworkrpc.server.ServerFactory;

@RpcComponent(name = "netty")
public class NettyServerFactory implements ServerFactory {
	@Override
	public Server getServer(URL url) {
		RpcInstanceFactory rpcInstanceFactory = ExtensionLoader.getExtensionLoader(RpcInstanceFactory.class)
				.getExtension(url.getParameter(RpcConstant.TRANSPORTER), Scope.SINGLETON);
		return new NettyServer(url, rpcInstanceFactory);
	}
}
