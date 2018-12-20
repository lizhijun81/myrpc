package com.myrpc.rpc;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;

public class RpcContext {

	private static TransmittableThreadLocal<RpcContext> LOCAL_CONTEXT = new TransmittableThreadLocal<>();

	private RpcRequest request;
	private RpcResponse response;
	private String clientRequesterId = null;

	public static RpcContext getContext() {
		return LOCAL_CONTEXT.get();
	}

	public static RpcContext init(RpcRequest request) {
		RpcContext context = new RpcContext();
		if (request != null) {
			context.setRequest(request);
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

	public String getRequesterId() {
		if (clientRequesterId != null) {
			return clientRequesterId;
		} else {
			return request == null ? null : String.valueOf(request.getRequestId());
		}
	}

	public RpcRequest getRequest() {
		return request;
	}

	public void setRequest(RpcRequest request) {
		this.request = request;
	}

	public RpcResponse getResponse() {
		return response;
	}

	public void setResponse(RpcResponse response) {
		this.response = response;
	}

	public String getClientRequesterId() {
		return clientRequesterId;
	}

	public void setClientRequesterId(String clientRpcRequesterId) {
		this.clientRequesterId = clientRequesterId;
	}
}
