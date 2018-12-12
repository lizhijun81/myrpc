package com.frameworkrpc.loadbalance.impl;

import com.frameworkrpc.common.MathUtil;
import com.frameworkrpc.extension.RpcComponent;
import com.frameworkrpc.loadbalance.LoadBalance;
import com.frameworkrpc.model.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RpcComponent(name = "roundrobin")
public class RoundRobinLoadBalance implements LoadBalance {

	private AtomicInteger idx = new AtomicInteger(0);

	@Override
	public URL select(List<URL> urls) {
		int index = getNextNonNegative();
		for (int i = 0; i < urls.size(); i++) {
			URL ref = urls.get((i + index) % urls.size());
				return ref;
		}
		return null;
	}

	private int getNextNonNegative() {
		return MathUtil.getNonNegative(idx.incrementAndGet());
	}
}
