package com.frameworkrpc.exception;

public class MyRpcTimeOutException extends RuntimeException {

	public MyRpcTimeOutException() {
		super();
	}

	public MyRpcTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRpcTimeOutException(String message) {
		super(message);
	}

	public MyRpcTimeOutException(Throwable cause) {
		super(cause);
	}
}
