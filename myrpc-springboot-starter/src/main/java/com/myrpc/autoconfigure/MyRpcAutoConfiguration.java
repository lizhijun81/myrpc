package com.myrpc.autoconfigure;

import com.myrpc.boot.config.ApplicationBootConfig;
import com.myrpc.boot.config.ProtocolBootConfig;
import com.myrpc.boot.config.RegistryBootConfig;
import com.myrpc.boot.config.ApplicationConfig;
import com.myrpc.boot.config.ProtocolConfig;
import com.myrpc.boot.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRpcAutoConfiguration {

	@Bean
	ApplicationConfig applicationConfig() {
		return new ApplicationBootConfig();
	}

	@Bean
	ProtocolConfig protocolConfig() {
		return new ProtocolBootConfig();
	}

	@Bean
	RegistryConfig registryConfig() {
		return new RegistryBootConfig();
	}
}
