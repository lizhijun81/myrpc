package com.frameworkrpc.exception;

public class MyRpcInvokeException extends  RuntimeException {

	public MyRpcInvokeException() {
		super();
	}

	public MyRpcInvokeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRpcInvokeException(String message) {
		super(message);
	}

	public MyRpcInvokeException(Throwable cause) {
		super(cause);
	}
}

