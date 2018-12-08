package com.frameworkrpc.server;

import com.frameworkrpc.model.URL;

public interface Server {

	URL getUrl();

	boolean isOpened();

	boolean isClosed();

	void doOpen();

	void doClose();
}
