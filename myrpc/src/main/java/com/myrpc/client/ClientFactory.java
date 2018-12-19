package com.myrpc.client;

import com.myrpc.config.URL;

public interface ClientFactory {

	Client getClient(URL url);
}
