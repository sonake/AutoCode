/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.sonake.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sonake.AutoCodeProperties;
import com.sonake.dao.AutoCodeDao;
import com.sonake.utils.AutoCodeUtils;
import com.sonake.utils.PageUtils;
import com.sonake.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
@Slf4j
public class AutoCodeService {
    @Autowired
    private AutoCodeDao autoCodeDao;

    @Autowired
    private AutoCodeProperties autoCodeProperties;

    public PageUtils queryList(Query query) {
        Page<?> page = PageHelper.startPage(query.getPage(), query.getLimit());
        //log.info(JSON.toJSONString(query));
        List<Map<String, Object>> list = autoCodeDao.queryList(query);
        log.info(JSON.toJSONString(autoCodeProperties));
        return new PageUtils(list, (int) page.getTotal(), query.getLimit(), query.getPage());
    }

    public Map<String, String> queryTable(String tableName) {
        return autoCodeDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return autoCodeDao.queryColumns(tableName);
    }

    public boolean generatorCode(String[] tableNames, Map<String, String> params) {
        int size = 0;
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            if (AutoCodeUtils.generatorCode(table, columns, params)) {
                size++;
            }
            ;
        }
        if (tableNames.length == size) {
            return true;
        } else {
            return false;
        }
    }
}
