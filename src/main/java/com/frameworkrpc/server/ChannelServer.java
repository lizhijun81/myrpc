package com.frameworkrpc.server;

import com.frameworkrpc.model.URL;

public interface ChannelServer {

	URL getUrl();

	boolean isOpen();

	boolean isClose();

	boolean isBound();

	void doOpen();

	void doClose();
}
