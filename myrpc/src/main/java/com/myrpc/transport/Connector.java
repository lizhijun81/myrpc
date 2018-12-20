package com.myrpc.transport;

import com.myrpc.config.URL;
import com.myrpc.model.RpcRequest;

import java.net.InetSocketAddress;

public interface Connector {

	URL url();

	Connector with(URL url);

	Connector init();

	InetSocketAddress getConnectAddress();

	int getConnectTimeout();

	boolean isClosed();

	boolean isConnected();

	void close();

	void connect();

	void disConnect();

	void request(RpcRequest request);
}
