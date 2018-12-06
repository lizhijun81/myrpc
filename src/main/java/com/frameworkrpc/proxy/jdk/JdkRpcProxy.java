package com.frameworkrpc.proxy.jdk;

import com.frameworkrpc.client.ChannelClient;
import com.frameworkrpc.client.ChannelClientFactory;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.RpcProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class JdkRpcProxy implements RpcProxy {

	@Override
	public <T> T getInstance(Class<T> inf, URL url) {
		return (T) Proxy.newProxyInstance(inf.getClassLoader(), new Class[]{inf}, new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				if (Object.class == method.getDeclaringClass()) {
					String name = method.getName();
					if ("equals".equals(name)) {
						return proxy == args[0];
					} else if ("hashCode".equals(name)) {
						return System.identityHashCode(proxy);
					} else if ("toString".equals(name)) {
						return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy)) + ", with InvocationHandler "
								+ this;
					} else {
						throw new IllegalStateException(String.valueOf(method));
					}
				}

				RpcRequester request = new RpcRequester();
				request.setRequestId(UUID.randomUUID().toString());
				request.setInterfaceName(method.getDeclaringClass().getName());
				request.setMethodName(method.getName());
				request.setParameterTypes(method.getParameterTypes());
				request.setParameters(args);

				ChannelClient channelClient = ChannelClientFactory.createClient(url);
				if (!channelClient.isOpened())
					channelClient.doClose();
				if (!channelClient.isConnected())
					channelClient.doClose();
				return channelClient.call(request);
			}
		});
	}
}

