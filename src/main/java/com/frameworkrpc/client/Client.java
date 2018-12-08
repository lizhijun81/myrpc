package com.frameworkrpc.client;

import com.frameworkrpc.model.URL;

public interface Client {

	URL getUrl();

	boolean isOpened();

	boolean isClosed();

	boolean isConnected();

	void doOpen();

	void doClose();

	void doConnect();

	void disConnect();

}
