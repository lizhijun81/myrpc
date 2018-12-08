package com.frameworkrpc.server;

import com.frameworkrpc.model.URL;

public interface ServerFactory {

	Server getServer(URL url);
}
