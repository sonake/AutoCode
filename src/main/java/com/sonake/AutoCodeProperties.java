package com.sonake;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author bby
 * @date 2019/9/11
 * 代码生成配置类
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "autocode")
public class AutoCodeProperties {
    /**
     * 数据库类型，目前支持【mysql、oracle、sqlserver、postgresql】
     */
    public static String DEFAULT_DATABASETYPE="mysql";
    private String databaseType=DEFAULT_DATABASETYPE;
}
