package com.myrpc.rpc;

import java.lang.reflect.Method;

public interface RpcInstanceFactory {

	void setRpcInstance(String infName, Object obj);

	Object getRpcInstance(String infName);

	Method getMethod(String infName, String methodName);
}
