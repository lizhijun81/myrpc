package com.myrpc.client;

import com.myrpc.model.RpcRequest;
import com.myrpc.model.URL;

public interface Client {

	URL getUrl();

	boolean isClosed();

	boolean isConnected();

	void doClose();

	void doConnect();

	void disConnect();

	void request(RpcRequest request);

}
