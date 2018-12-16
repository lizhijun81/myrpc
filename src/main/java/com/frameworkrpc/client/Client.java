package com.frameworkrpc.client;

import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.URL;

public interface Client {

	URL getUrl();

	boolean isClosed();

	boolean isConnected();

	void doClose();

	void doConnect();

	void disConnect();

	void request(RpcRequest request);

}
