package com.sonake;

import com.sonake.config.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AutoCodeProperties.class)
@MapperScan("com.sonake.dao")
@ComponentScan(basePackages = {"com.sonake.*"})
public class AutoCodeConfiguration {

}
