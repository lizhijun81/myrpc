package com.myrpc.consumer.cluster;

import com.myrpc.config.URL;
import com.myrpc.registry.RegistryListener;

import java.util.List;

public interface LoadBalance {

	LoadBalance with(RegistryListener registryListener);

	URL getProviderUrl();

	URL select(List<URL> serverNodes);
}
