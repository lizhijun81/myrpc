package com.frameworkrpc.exception;

public class CommonRpcException extends  RuntimeException {

	public CommonRpcException() {
		super();
	}

	public CommonRpcException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonRpcException(String message) {
		super(message);
	}

	public CommonRpcException(Throwable cause) {
		super(cause);
	}
}
