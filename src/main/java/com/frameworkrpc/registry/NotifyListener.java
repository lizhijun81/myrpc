package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;

public interface NotifyListener {

	void notify(List<URL> urls);

}
