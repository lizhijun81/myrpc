package com.frameworkrpc.proxy.jdk;

import com.frameworkrpc.client.Client;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.AbstractProxy;
import com.frameworkrpc.proxy.ProxyFactory;
import com.google.common.reflect.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

@RpcComponent(name = "jdk")
public class JdkProxyFactory extends AbstractProxy implements ProxyFactory {

	private static final long serialVersionUID = -1019177743257040864L;
	private static final Logger logger = LoggerFactory.getLogger(JdkProxyFactory.class);

	@Override
	public <T> T getInstance(Class<T> inf, URL url) {
		return (T) Reflection.newProxy(inf, new InvocationHandler() {
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

				Client client = getClient(url);

				return client.request(request).get();
			}
		});
	}

	@Override
	public void initProxy(URL url) {
		super.initProxy(url);
	}
}

