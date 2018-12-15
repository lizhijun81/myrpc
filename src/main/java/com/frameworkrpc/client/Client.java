package com.frameworkrpc.client;

import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.consumer.future.InvokeFuture;

public interface Client {

	URL getUrl();

	boolean isClosed();

	boolean isConnected();

	void doClose();

	void doConnect();

	void disConnect();

	InvokeFuture request(RpcRequest request);

}
