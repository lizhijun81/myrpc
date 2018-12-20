package com.myrpc.exception;

public class MyRpcServerRpcException extends RuntimeException {

	public MyRpcServerRpcException() {
		super();
	}

	public MyRpcServerRpcException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRpcServerRpcException(String message) {
		super(message);
	}

	public MyRpcServerRpcException(Throwable cause) {
		super(cause);
	}
}

