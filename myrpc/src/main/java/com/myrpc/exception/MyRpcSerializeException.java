package com.myrpc.exception;

public class MyRpcSerializeException extends  RuntimeException {

	public MyRpcSerializeException() {
		super();
	}

	public MyRpcSerializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRpcSerializeException(String message) {
		super(message);
	}

	public MyRpcSerializeException(Throwable cause) {
		super(cause);
	}
}
