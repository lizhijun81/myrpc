package com.frameworkrpc.server;

import com.frameworkrpc.model.URL;

public interface ChannelServer {

	URL getUrl();

	boolean isOpened();

	boolean isClosed();

	boolean isBound();

	void doOpen();

	void doClose();
}
