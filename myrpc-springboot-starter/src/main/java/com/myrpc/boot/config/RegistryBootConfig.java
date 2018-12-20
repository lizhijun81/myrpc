package com.myrpc.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "myrpc.registry")
public class RegistryBootConfig extends RegistryConfig {
}
