package com.frameworkrpc.proxy;

import com.frameworkrpc.model.URL;

public interface ProxyFactory {

	ClassProxy getClassProxy(URL url);
}
