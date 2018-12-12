package com.frameworkrpc.rpc;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;

public class RpcContext {

	private static TransmittableThreadLocal<RpcContext> LOCAL_CONTEXT = new TransmittableThreadLocal<>();

	private RpcRequester request;
	private RpcResponse response;
	private String clientRpcRequesterId = null;

	public static RpcContext getContext() {
		return LOCAL_CONTEXT.get();
	}

	public static RpcContext init(RpcRequester request) {
		RpcContext context = new RpcContext();
		if (request != null) {
			context.setRpcRequest(request);
		}
		LOCAL_CONTEXT.set(context);
		return context;
	}

	public static RpcContext init() {
		RpcContext context = new RpcContext();
		LOCAL_CONTEXT.set(context);
		return context;
	}

	public static void destroy() {
		LOCAL_CONTEXT.remove();
	}

	public String getRpcRequesterId() {
		if (clientRpcRequesterId != null) {
			return clientRpcRequesterId;
		} else {
			return request == null ? null : String.valueOf(request.getRequestId());
		}
	}

	public RpcRequester getRpcRequest() {
		return request;
	}

	public void setRpcRequest(RpcRequester request) {
		this.request = request;
	}

	public RpcResponse getResponse() {
		return response;
	}

	public void setResponse(RpcResponse response) {
		this.response = response;
	}

	public String getClientRpcRequesterId() {
		return clientRpcRequesterId;
	}

	public void setClientRpcRequesterId(String clientRpcRequesterId) {
		this.clientRpcRequesterId = clientRpcRequesterId;
	}
}
