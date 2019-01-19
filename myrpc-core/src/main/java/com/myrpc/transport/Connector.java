package com.myrpc.transport;

import com.myrpc.config.URL;
import com.myrpc.rpc.Channel;

public interface Connector {

	URL url();

	Connector with(URL url);

	Connector init();

	int getConnectTimeout();

	void close();

	Channel connect(URL url);

}
