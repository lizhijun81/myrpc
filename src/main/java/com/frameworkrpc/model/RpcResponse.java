package com.frameworkrpc.model;

import java.io.Serializable;

public class RpcResponse implements Serializable {

	private static final long serialVersionUID = -2638927263470157485L;
	private String requestId;
	private Exception error;
	private Object result;
	private long processTime;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public long getProcessTime() {
		return processTime;
	}

	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}

}
