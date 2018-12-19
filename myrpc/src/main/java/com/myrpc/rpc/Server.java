package com.myrpc.rpc;

import com.myrpc.config.ServiceConfig;
import com.myrpc.config.URL;
import com.myrpc.registry.Registry;
import com.myrpc.transport.Acceptor;

import java.util.List;

public interface Server {

	URL url();

	Server with(ServiceConfig serviceConfig);

	Server with(URL url);

	Server init();

	Acceptor acceptor();

	Registry registry();

	void publish(ServiceConfig serviceConfig);

	void publish(List<ServiceConfig> serviceConfigs);

	void publishAll();

	void unpublish(ServiceConfig serviceConfig);

	void unpublish(List<ServiceConfig> serviceConfigs);

	void unpublishAll();

	void start();

	void shutdownGracefully();

}
