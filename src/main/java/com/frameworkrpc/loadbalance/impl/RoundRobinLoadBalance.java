package com.frameworkrpc.loadbalance.impl;

import com.frameworkrpc.common.MathUtils;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.loadbalance.LoadBalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RpcComponent(name = "roundrobin")
public class RoundRobinLoadBalance implements LoadBalance {

	private AtomicInteger idx = new AtomicInteger(0);

	private int getNextNonNegative() {
		return MathUtils.getNonNegative(idx.incrementAndGet());
	}

	@Override
	public String select(List<String> serverNodes) {
		int index = getNextNonNegative();
		for (int i = 0; i < serverNodes.size(); i++) {
			return serverNodes.get((i + index) % serverNodes.size());
		}
		return null;
	}
}
