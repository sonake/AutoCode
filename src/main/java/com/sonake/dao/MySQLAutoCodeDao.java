package com.sonake.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * MySQL代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2018-07-24
 */
public interface MySQLAutoCodeDao extends AutoCodeDao {

    @Select("<script>\n"+
            "select table_name tableName, engine, table_comment tableComment, create_time createTime from\n" +
            "        information_schema.tables\n" +
            "        where table_schema = (select database())\n" +
            "        <if test=\"tableName != null and tableName.trim() != ''\">\n" +
            "            and table_name like concat('%', #{tableName}, '%')\n" +
            "        </if>\n" +
            "        order by create_time desc\n"+
            "</script>" )
    List<Map<String, Object>> queryList(Map<String, Object> map);

    @Select("select table_name tableName, engine, table_comment tableComment, create_time createTime from\n" +
            "        information_schema.tables\n" +
            "        where table_schema = (select database()) and table_name = #{tableName}")
    Map<String, String> queryTable(String tableName);

    @Select("select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra\n" +
            "        from information_schema.columns\n" +
            "        where table_name = #{tableName} and table_schema = (select database()) order by ordinal_position")
    List<Map<String, String>> queryColumns(String tableName);

}
