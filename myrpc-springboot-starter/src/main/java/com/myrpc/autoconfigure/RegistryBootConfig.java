package com.myrpc.autoconfigure;

import com.myrpc.config.RegistryConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myrpc.registry")
public class RegistryBootConfig extends RegistryConfig {
}
