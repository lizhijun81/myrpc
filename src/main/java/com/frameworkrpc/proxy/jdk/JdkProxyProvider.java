package com.frameworkrpc.proxy.jdk;

import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.AbstractProxy;
import com.frameworkrpc.proxy.ClassProxy;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@RpcComponent(name = "jdk")
public class JdkProxyProvider extends AbstractProxy implements ClassProxy {

	private static final long serialVersionUID = -1019177743257040864L;
	private static final Logger logger = LoggerFactory.getLogger(JdkProxyProvider.class);

	protected JdkProxyProvider(URL url) {
		super(url);
	}

	@Override
	public <T> T getInstance(Class<T> inf) {
		return (T) Reflection.newProxy(inf, new AbstractInvocationHandler() {
			@Override
			protected Object handleInvocation(Object proxy, Method method, Object[] args) {
               return invocation(proxy,method,args);
			}
		});
	}

}

