package com.frameworkrpc.rpc.netty;

import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.exception.MyRpcRpcException;
import com.frameworkrpc.rpc.RpcInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RpcComponent(name = "netty")
public class NettyRpcInstanceFatoryImpl implements RpcInstanceFactory {

	private final static Logger logger = LoggerFactory.getLogger(NettyRpcInstanceFatoryImpl.class);

	private Map<String, Object> instances = new HashMap<String, Object>();

	private Map<String, Map<String, Method>> methodMap = new HashMap<String, Map<String, Method>>();

	@Override
	public synchronized void setRpcInstance(String infName, Object obj) {
		instances.put(infName, obj);
	}

	@Override
	public Object getRpcInstance(String infName) throws MyRpcRpcException {
		Object obj = instances.get(infName);
		return obj;
	}


	public Method getMethod(String infName, String methodName) {
		Map<String, Method> map = this.methodMap.get(infName);
		if (map != null) {
			return map.get(methodName);
		} else {
			synchronized (this) {
				try {
					Object obj = instances.get(infName);
					Method[] infMethods = Class.forName(infName).getMethods();
					Map<String, Method> methodMap = new HashMap<String, Method>();
					for (Method infmth : infMethods) {
						Method m = obj.getClass().getMethod(infmth.getName(), infmth.getParameterTypes());
						String _methodName = m.getName();
						if (methodMap.containsKey(_methodName)) {
							logger.error("Duplicated method " + _methodName + " found in " + obj.getClass().getName());
						} else {
							methodMap.put(m.getName(), m);
						}
					}
					this.methodMap.put(infName, methodMap);
					return map.get(methodName);
				} catch (NoSuchMethodException e1) {
					throw new MyRpcRpcException(e1.getMessage(), e1);
				} catch (ClassNotFoundException e) {
					throw new MyRpcRpcException(e.getMessage(), e);
				}
			}
		}

	}

}
