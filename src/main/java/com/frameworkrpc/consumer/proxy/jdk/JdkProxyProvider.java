package com.frameworkrpc.consumer.proxy.jdk;

import com.frameworkrpc.consumer.proxy.AbstractProxy;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.RpcRequest;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.UUID;

@RpcComponent(name = "jdk")
public class JdkProxyProvider extends AbstractProxy {

	private static final Logger logger = LoggerFactory.getLogger(JdkProxyProvider.class);

	@Override
	public <T> T getInstance(Class<T> inf) {
		return (T) Reflection.newProxy(inf, new AbstractInvocationHandler() {
			@Override
			protected Object handleInvocation(Object proxy, Method method, Object[] args) {
				RpcRequest request = new RpcRequest();
				request.setRequestId(UUID.randomUUID().toString());
				request.setInterfaceName(method.getDeclaringClass().getName());
				request.setMethodName(method.getName());
				request.setParameterTypes(method.getParameterTypes());
				request.setParameters(args);
				return JdkProxyProvider.super.invoke(request, method.getReturnType());
			}
		});
	}

}

