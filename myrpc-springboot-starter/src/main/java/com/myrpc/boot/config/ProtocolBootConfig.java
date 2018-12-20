package com.myrpc.boot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "myrpc.protocol")
public class ProtocolBootConfig extends ProtocolConfig {
}
