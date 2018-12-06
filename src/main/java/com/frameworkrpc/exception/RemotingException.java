package com.frameworkrpc.exception;

public class RemotingException extends  RuntimeException {

	public RemotingException() {
		super();
	}

	public RemotingException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemotingException(String message) {
		super(message);
	}

	public RemotingException(Throwable cause) {
		super(cause);
	}
}
