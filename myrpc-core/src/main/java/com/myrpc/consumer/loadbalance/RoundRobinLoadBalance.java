package com.myrpc.consumer.loadbalance;

import com.myrpc.common.MathUtils;
import com.myrpc.extension.RpcComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RpcComponent(name = "roundrobin")
public class RoundRobinLoadBalance implements LoadBalance {

	private static final Logger logger = LoggerFactory.getLogger(RoundRobinLoadBalance.class);

	private AtomicInteger idx = new AtomicInteger(0);

	private int getNextNonNegative() {
		return MathUtils.getNonNegative(idx.incrementAndGet());
	}

	@Override
	public String select(List<String> serverNodes) {
		int length = serverNodes.size();
		if (length == 0)
			return null;

		if (length == 1)
			return serverNodes.get(0);

		int index = getNextNonNegative();
		for (int i = 0; i < serverNodes.size(); i++) {
			String serverNode = serverNodes.get((i + index) % serverNodes.size());
			logger.info("current serverNode {}", serverNode);
			return serverNode;
		}
		return null;
	}
}
