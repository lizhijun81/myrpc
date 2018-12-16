package com.frameworkrpc.exception;

public class MyServerRpcException extends RuntimeException {

	public MyServerRpcException() {
		super();
	}

	public MyServerRpcException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyServerRpcException(String message) {
		super(message);
	}

	public MyServerRpcException(Throwable cause) {
		super(cause);
	}
}

