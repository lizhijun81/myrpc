package com.frameworkrpc.exception;

public class InvokeException extends  RuntimeException {

	public InvokeException() {
		super();
	}

	public InvokeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvokeException(String message) {
		super(message);
	}

	public InvokeException(Throwable cause) {
		super(cause);
	}
}

