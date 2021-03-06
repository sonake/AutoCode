/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.sonake.config;

import com.sonake.utils.RRException;
import com.sonake.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

/**
 * @author ：xzyuan
 * @date ：Created in 2019/9/18 9:43
 * @description：数据库配置
 * @version: 1.0
 */
@Configuration
public class DbConfig {
    @Value("${auto-code.database-type: mysql}")
    private String database;
    @Autowired
    private MySQLAutoCodeDao mySQLAutoCodeDao;
    @Autowired
    private OracleAutoCodeDao oracleAutoCodeDao;
    @Autowired
    private SQLServerAutoCodeDao sqlServerAutoCodeDao;
    @Autowired
    private PostgreSQLAutoCodeDao postgreSQLAutoCodeDao;

    @Bean
    @Primary
    public AutoCodeDao getGeneratorDao() {
        if ("mysql".equalsIgnoreCase(database)) {
            return mySQLAutoCodeDao;
        } else if ("oracle".equalsIgnoreCase(database)) {
            return oracleAutoCodeDao;
        } else if ("sqlserver".equalsIgnoreCase(database)) {
            return sqlServerAutoCodeDao;
        } else if ("postgresql".equalsIgnoreCase(database)) {
            return postgreSQLAutoCodeDao;
        } else {
            throw new RRException("不支持当前数据库：" + database);
        }
    }
}
