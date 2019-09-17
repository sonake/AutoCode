package com.sonake.dao;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * SQLServer代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 * @Mapper
 * @since 2018-07-24
 */
public interface SQLServerAutoCodeDao extends AutoCodeDao {
    @Select("<script>\n" +
            "select * from\n" +
            "        (\n" +
            "        select cast(so.name as varchar(500)) as tableName, cast(sep.value as varchar(500)) as tableComment, getDate() as\n" +
            "        createTime\n" +
            "        from sysobjects so\n" +
            "        left JOIN sys.extended_properties sep\n" +
            "        on sep.major_id=so.id and sep.minor_id=0\n" +
            "        where (xtype='U' or xtype='v')\n" +
            "        ) t where 1=1\n" +
            "        <if test=\"tableName != null and tableName.trim() != ''\">\n" +
            "            and t.tableName like concat('%', #{tableName}, '%')\n" +
            "        </if>\n" +
            "        order by t.tableName\n" +
            "</script>")
    List<Map<String, Object>> queryList(Map<String, Object> map);

    @Select("select * from (\n" +
            "        select cast(so.name as varchar(500)) as tableName, 'mssql' as engine,cast(sep.value as varchar(500)) as\n" +
            "        tableComment, getDate() as createTime\n" +
            "        from sysobjects so\n" +
            "        left JOIN sys.extended_properties sep on sep.major_id=so.id and sep.minor_id=0\n" +
            "        where (xtype='U' or xtype='v')\n" +
            "        ) t where t.tableName=#{tableName}")
    Map<String, String> queryTable(String tableName);

    @Select(" SELECT\n" +
            "        cast(\n" +
            "        b.NAME AS VARCHAR(500)\n" +
            "        ) AS columnName,\n" +
            "        cast(\n" +
            "        sys.types.NAME AS VARCHAR(500)\n" +
            "        ) AS dataType,\n" +
            "        cast(\n" +
            "        c.VALUE AS VARCHAR(500)\n" +
            "        ) AS columnComment,\n" +
            "        (\n" +
            "        SELECT\n" +
            "        CASE\n" +
            "        count( 1 )\n" +
            "        WHEN 1 then 'PRI'\n" +
            "        ELSE ''\n" +
            "        END\n" +
            "        FROM\n" +
            "        syscolumns,\n" +
            "        sysobjects,\n" +
            "        sysindexes,\n" +
            "        sysindexkeys,\n" +
            "        systypes\n" +
            "        WHERE\n" +
            "        syscolumns.xusertype = systypes.xusertype\n" +
            "        AND syscolumns.id = object_id(A.NAME)\n" +
            "        AND sysobjects.xtype = 'PK'\n" +
            "        AND sysobjects.parent_obj = syscolumns.id\n" +
            "        AND sysindexes.id = syscolumns.id\n" +
            "        AND sysobjects.NAME = sysindexes.NAME\n" +
            "        AND sysindexkeys.id = syscolumns.id\n" +
            "        AND sysindexkeys.indid = sysindexes.indid\n" +
            "        AND syscolumns.colid = sysindexkeys.colid\n" +
            "        AND syscolumns.NAME = B.NAME\n" +
            "        ) as columnKey,\n" +
            "        '' as extra\n" +
            "        FROM\n" +
            "        (\n" +
            "        select\n" +
            "        name,\n" +
            "        object_id\n" +
            "        from\n" +
            "        sys.tables\n" +
            "        UNION all select\n" +
            "        name,\n" +
            "        object_id\n" +
            "        from\n" +
            "        sys.views\n" +
            "        ) a\n" +
            "        INNER JOIN sys.COLUMNS b ON\n" +
            "        b.object_id = a.object_id\n" +
            "        LEFT JOIN sys.types ON\n" +
            "        b.user_type_id = sys.types.user_type_id\n" +
            "        LEFT JOIN sys.extended_properties c ON\n" +
            "        c.major_id = b.object_id\n" +
            "        AND c.minor_id = b.column_id\n" +
            "        WHERE\n" +
            "        a.NAME = #{tableName}\n" +
            "        and sys.types.NAME != 'sysname'")
    List<Map<String, String>> queryColumns(String tableName);
}
