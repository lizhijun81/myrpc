package com.frameworkrpc.proxy.jdk;

import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.ClassProxy;
import com.frameworkrpc.proxy.ProxyFactory;

@RpcComponent(name = "jdk")
public class JdkProxyFactory implements ProxyFactory {
	@Override
	public ClassProxy getClassProxy(URL url) {
		return new JdkProxyProvider(url);
	}
}
