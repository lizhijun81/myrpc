package com.myrpc.server;

import com.myrpc.model.URL;

public interface ServerFactory {

	Server getServer(URL url);
}
