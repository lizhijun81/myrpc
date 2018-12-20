package com.myrpc.autoconfigure;

import com.myrpc.config.ApplicationConfig;
import com.myrpc.config.ProtocolConfig;
import com.myrpc.config.RegistryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRpcAutoConfiguration {

	@Bean
	ApplicationConfig applicationConfig(@Autowired ApplicationBootConfig applicationBootConfig) {
		return applicationBootConfig;
	}

	@Bean
	ProtocolConfig protocolConfig(@Autowired ProtocolBootConfig protocolBootConfig) {
		return protocolBootConfig;
	}

	@Bean
	RegistryConfig registryConfig(@Autowired RegistryBootConfig registryBootConfig) {
		return registryBootConfig;
	}

}
