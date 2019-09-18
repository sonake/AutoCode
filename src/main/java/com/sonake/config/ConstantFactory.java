package com.sonake.config;

import com.sonake.AutoCodeProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * @author ：xzyuan
 * @date ：Created in 2019/9/18 9:43
 * @description：常量生产
 * @version: 1.0
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory {

    //获取AutoCodeProperties
    public static AutoCodeProperties getAutoCodeProperties(){
        return SpringContextHolder.getBean(AutoCodeProperties.class);
    }
}
