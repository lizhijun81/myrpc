package com.frameworkrpc.client;

import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;

public interface ChannelClient {

	URL getUrl();

	boolean isOpened();

	boolean isClosed();

	boolean isConnected();

	void doOpen();

	void doClose();

	void doConnect();

	void disConnect();

	RpcResponse call(RpcRequester request);

}
