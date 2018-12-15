package com.frameworkrpc.consumer.loadbalance;

import java.util.List;

public interface LoadBalance {

	String select(List<String> serverNodes);
}
