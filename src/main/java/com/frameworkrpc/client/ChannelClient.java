package com.frameworkrpc.client;

import com.frameworkrpc.model.URL;

public interface ChannelClient {

	URL getUrl();

	boolean isOpen();

	boolean isClosed();


	void doOpen();

	void doClose();

	void doConnect();

	void disConnect();



}
