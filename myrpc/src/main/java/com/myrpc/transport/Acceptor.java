package com.myrpc.transport;

import com.myrpc.config.URL;

import java.net.SocketAddress;

public interface Acceptor {

	URL url();

	Acceptor with(URL url);

	Acceptor init();

	SocketAddress localAddress();

	boolean isStarted();

	boolean isShutdowned();

	void start();

	void shutdownGracefully();
}
