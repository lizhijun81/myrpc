package com.myrpc.server.netty;

import com.myrpc.extension.RpcComponent;
import com.myrpc.common.RpcConstants;
import com.myrpc.extension.Scope;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.model.URL;
import com.myrpc.rpc.RpcInstanceFactory;
import com.myrpc.server.Server;
import com.myrpc.server.ServerFactory;

@RpcComponent(name = "netty")
public class NettyServerFactory implements ServerFactory {
	@Override
	public Server getServer(URL url) {
		RpcInstanceFactory rpcInstanceFactory = ExtensionLoader.getExtensionLoader(RpcInstanceFactory.class)
				.getExtension(url.getParameter(RpcConstants.TRANSPORTER_KEY), Scope.SINGLETON);
		return new NettyServer(url, rpcInstanceFactory);
	}
}
