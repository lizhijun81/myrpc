package com.myrpc.exception;

public class MyRpcRpcException extends  RuntimeException {

	public MyRpcRpcException() {
		super();
	}

	public MyRpcRpcException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRpcRpcException(String message) {
		super(message);
	}

	public MyRpcRpcException(Throwable cause) {
		super(cause);
	}
}
