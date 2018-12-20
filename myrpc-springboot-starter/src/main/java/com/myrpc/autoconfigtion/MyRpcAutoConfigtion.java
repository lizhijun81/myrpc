package com.myrpc.autoconfigtion;

import com.myrpc.config.ApplicationConfig;
import com.myrpc.config.ProtocolConfig;
import com.myrpc.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRpcAutoConfigtion {

	@Bean
	ApplicationConfig applicationConfig() {
		return new ApplicationConfig();
	}

	@Bean
	ProtocolConfig protocolConfig() {
		return new ProtocolConfig();
	}

	@Bean
	RegistryConfig registryConfig() {
		return new RegistryConfig();
	}
}
