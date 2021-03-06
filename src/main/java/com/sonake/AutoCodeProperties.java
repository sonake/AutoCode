package com.sonake;


import com.sonake.utils.DataBaseTypeEnum;
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
@ConfigurationProperties(prefix = "auto-code")
public class AutoCodeProperties {
    /**
     * 数据库类型，目前支持【mysql、oracle、sqlserver、postgresql】
     */
    private DataBaseTypeEnum databaseType= DataBaseTypeEnum.MYSQL;

    /**
     *
     */
    private String mainPath;
    /**
     * groupId
     */
    private String groupId="com.sonake";
    /**
     *  artifactId
     */
    private String artifactId="ac";
    /**
     * author
     */
    private String author="bby";
    /**
     * email
     */
    private String email="yxzbby@aliyun.com";
    /**
     * 表前缀
     */
    private String tablePrefix;
    /**
     * 模板路径
     */
    private String vmUrl;



}
