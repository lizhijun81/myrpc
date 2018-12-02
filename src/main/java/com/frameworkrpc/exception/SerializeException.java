package com.frameworkrpc.exception;

public class SerializeException extends  RuntimeException {

	public SerializeException() {
		super();
	}

	public SerializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializeException(String message) {
		super(message);
	}

	public SerializeException(Throwable cause) {
		super(cause);
	}
}
