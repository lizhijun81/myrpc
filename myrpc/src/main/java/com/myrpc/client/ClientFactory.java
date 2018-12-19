package com.myrpc.client;

import com.myrpc.model.URL;

public interface ClientFactory {

	Client getClient(URL url);
}
