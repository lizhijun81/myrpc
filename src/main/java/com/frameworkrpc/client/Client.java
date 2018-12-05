package com.frameworkrpc.client;

import com.frameworkrpc.model.URL;

public interface Client {

	URL getURL();

	boolean isOpen();

	boolean isClose();

	void doOpen();

	void doClose();

	void doconnect();

	void disconnect();

	void reconnect();

}
