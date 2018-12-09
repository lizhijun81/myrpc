package com.frameworkrpc.loadbalance;

import com.frameworkrpc.model.URL;

import java.util.List;

public interface LoadBalance {

	URL select(List<URL> urls);
}
