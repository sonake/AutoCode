package com.sonake;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author ：xzyuan
 * @date ：Created in 2019/9/18 9:43
 * @description：starter配置
 * @version: 1.0
 */
@Configuration
@EnableConfigurationProperties(AutoCodeProperties.class)
@MapperScan("com.sonake.dao")
@ComponentScan(basePackages = {"com.sonake.*"})
public class AutoCodeConfiguration {

}
