package com.sonake.dao;

import java.util.List;
import java.util.Map;

/**
 * @author ：xzyuan
 * @date ：Created in 2019/9/18 9:43
 * @description：数据库接口
 * @version: 1.0
 */
public interface AutoCodeDao {
    List<Map<String, Object>> queryList(Map<String, Object> map);

    Map<String, String> queryTable(String tableName);

    List<Map<String, String>> queryColumns(String tableName);
}
