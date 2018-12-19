package com.myrpc.consumer.loadbalance;

import java.util.List;

public interface LoadBalance {

	String select(List<String> serverNodes);
}
