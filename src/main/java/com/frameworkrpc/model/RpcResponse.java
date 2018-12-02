package com.frameworkrpc.model;

import java.io.Serializable;

public class RpcResponse implements Serializable {

	private static final long serialVersionUID = -2638927263470157485L;
	private String requestId;
	private String error;
	private Object result;
	private boolean returnNotNull;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isReturnNotNull() {
		return returnNotNull;
	}

	public void setReturnNotNull(boolean returnNotNull) {
		this.returnNotNull = returnNotNull;
	}
}
