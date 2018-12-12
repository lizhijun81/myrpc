package com.frameworkrpc.loadbalance;

import java.util.List;

public interface LoadBalance {

	String select(List<String> serverNodes);
}
