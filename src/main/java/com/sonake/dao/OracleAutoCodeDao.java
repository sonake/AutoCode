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
public interface OracleAutoCodeDao extends AutoCodeDao {



    @Select("<script>\n"+
            "select dt.table_name tableName,\n" +
            "        dtc.comments tableComment,\n" +
            "        uo.created createTime\n" +
            "        from user_tables dt,\n" +
            "        user_tab_comments dtc,\n" +
            "        user_objects uo\n" +
            "        where dt.table_name = dtc.table_name and dt.table_name = uo.object_name and uo.object_type='TABLE'\n" +
            "        <if test=\"tableName != null and tableName.trim() != ''\">\n" +
            "            and dt.table_name like concat('%', UPPER(#{tableName}))\n" +
            "        </if>\n" +
            "        order by uo.CREATED desc\n"+
            "</script>")
    List<Map<String, Object>> queryList(Map<String, Object> map);

    @Select("select dt.table_name tableName,dtc.comments tableComment,dt.last_analyzed createTime from user_tables\n" +
            "        dt,user_tab_comments dtc where dt.table_name=dtc.table_name and dt.table_name = UPPER(#{tableName})")
    Map<String, String> queryTable(String tableName);

    @Select("select temp.column_name columnname,\n" +
            "        temp.data_type dataType,\n" +
            "        temp.comments columnComment,\n" +
            "        case temp.constraint_type when 'P' then 'PRI' when 'C' then 'UNI' else '' end \"COLUMNKEY\",\n" +
            "        '' \"EXTRA\"\n" +
            "        from (\n" +
            "        select col.column_id,\n" +
            "        col.column_name,\n" +
            "        col.data_type,\n" +
            "        colc.comments,\n" +
            "        uc.constraint_type,\n" +
            "        -- 去重\n" +
            "        row_number() over (partition by col.column_name order by uc.constraint_type desc) as row_flg\n" +
            "        from user_tab_columns col\n" +
            "        left join user_col_comments colc\n" +
            "        on colc.table_name = col.table_name\n" +
            "        and colc.column_name = col.column_name\n" +
            "        left join user_cons_columns ucc\n" +
            "        on ucc.table_name = col.table_name\n" +
            "        and ucc.column_name = col.column_name\n" +
            "        left join user_constraints uc\n" +
            "        on uc.constraint_name = ucc.constraint_name\n" +
            "        where col.table_name = upper(#{tableName})\n" +
            "        ) temp\n" +
            "        where temp.row_flg = 1\n" +
            "        order by temp.column_id")
    List<Map<String, String>> queryColumns(String tableName);
}
