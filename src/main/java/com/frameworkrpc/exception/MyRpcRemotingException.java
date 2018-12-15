package com.frameworkrpc.exception;

public class MyRpcRemotingException extends  RuntimeException {

	public MyRpcRemotingException() {
		super();
	}

	public MyRpcRemotingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyRpcRemotingException(String message) {
		super(message);
	}

	public MyRpcRemotingException(Throwable cause) {
		super(cause);
	}
}
