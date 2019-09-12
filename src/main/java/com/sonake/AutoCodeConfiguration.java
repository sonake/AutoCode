package com.sonake;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AutoCodeProperties.class)
public class AutoCodeConfiguration {

}
