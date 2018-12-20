package com.myrpc.autoconfigure;

import com.myrpc.config.ProtocolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myrpc.protocol")
public class ProtocolBootConfig extends ProtocolConfig {
}
