package com.frameworkrpc.server;

import com.frameworkrpc.model.URL;

public interface Server {

	URL getURL();

	boolean isOpen();

	boolean isClose();

	boolean isBound();

	void doOpen();

	void doClose();
}
