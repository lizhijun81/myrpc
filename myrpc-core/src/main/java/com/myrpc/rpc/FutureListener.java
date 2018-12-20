package com.myrpc.rpc;

public interface FutureListener<C> {

	void operationSuccess(C c) throws Exception;

	void operationFailure(C c, Throwable cause) throws Exception;
}
