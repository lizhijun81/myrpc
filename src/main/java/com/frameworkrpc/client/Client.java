package com.frameworkrpc.client;

import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.RPCFuture;

public interface Client {

	URL getUrl();

	boolean isClosed();

	boolean isConnected();

	void doClose();

	void doConnect();

	void disConnect();

	RPCFuture request(RpcRequester request);

}
