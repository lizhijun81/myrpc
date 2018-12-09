package com.frameworkrpc.client;

import com.frameworkrpc.model.URL;

public interface ClientFactory {

	Client getClient(URL url);
}
