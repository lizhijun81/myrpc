package com.frameworkrpc.rpc;

import com.frameworkrpc.exception.RpcException;

import java.lang.reflect.Method;

public interface NettyRpcInstanceFactory {

	void setRpcInstance(String infName, Object obj);

	Object getRpcInstance(String infName) throws RpcException;

	Method getMethod(String infName, String methodName);
}
