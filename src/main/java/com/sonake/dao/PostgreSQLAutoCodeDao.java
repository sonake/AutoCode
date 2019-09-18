package com.sonake.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author ：xzyuan
 * @date ：Created in 2019/9/18 9:43
 * @description：数据库接口
 * @version: 1.0
 */
public interface PostgreSQLAutoCodeDao extends AutoCodeDao {

    @Select("<script>\n"+
            "select t1.tablename as tableName, obj_description(relfilenode, 'pg_class') as tableComment, now() as createTime\n" +
            "        from pg_tables t1, pg_class t2\n" +
            "        where t1.tablename not like 'pg%' and t1.tablename not like 'sql_%' and t1.tablename = t2.relname\n" +
            "        <if test=\"tableName != null and tableName.trim() != ''\">\n" +
            "            and t1.tablename like concat('%', #{tableName}, '%')\n" +
            "        </if>\n" +
            "        order by t1.tablename desc\n"+
            "</script>")
    List<Map<String, Object>> queryList(Map<String, Object> map);
    @Select("select t1.tablename as tableName, obj_description(relfilenode, 'pg_class') as tableComment, now() as createTime\n" +
            "        from pg_tables t1, pg_class t2\n" +
            "        where t1.tablename = #{tableName} and t1.tablename = t2.relname")
    Map<String, String> queryTable(String tableName);
    @Select("select t2.attname as columnName, pg_type.typname as dataType, col_description(t2.attrelid,t2.attnum) as\n" +
            "        columnComment, '' as extra,\n" +
            "        (CASE t3.contype WHEN 'p' THEN 'PRI' ELSE '' END) as columnKey\n" +
            "        from pg_class as t1, pg_attribute as t2 inner join pg_type on pg_type.oid = t2.atttypid\n" +
            "        left join pg_constraint t3 on t2.attnum = t3.conkey[1] and t2.attrelid = t3.conrelid\n" +
            "        where t1.relname = #{tableName} and t2.attrelid = t1.oid and t2.attnum>0")
    List<Map<String, String>> queryColumns(String tableName);
}
