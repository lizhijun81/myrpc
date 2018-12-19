package com.myrpc.server;

import com.myrpc.model.URL;

public interface Server {

	URL getUrl();

	boolean isOpened();

	boolean isClosed();

	void doOpen();

	void doClose();
}
