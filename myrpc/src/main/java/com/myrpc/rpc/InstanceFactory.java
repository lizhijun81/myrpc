package com.myrpc.rpc;

import java.lang.reflect.Method;

public interface InstanceFactory {

	void setInstance(String infName, Object obj);

	Object getInstance(String infName);

	Method getMethod(String infName, String methodName);
}
