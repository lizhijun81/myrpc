package com.myrpc.autoconfigure;

import com.myrpc.config.ApplicationConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myrpc.appliction")
public class ApplicationBootConfig extends ApplicationConfig {

}
