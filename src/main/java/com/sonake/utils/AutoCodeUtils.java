package com.sonake.utils;

import com.alibaba.fastjson.JSON;
import com.sonake.AutoCodeProperties;
import com.sonake.config.ConstantFactory;
import com.sonake.entity.ColumnEntity;
import com.sonake.entity.TableEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * 代码生成工具类
 *
 * @author xzyuan
 * @email yxzbby@aliyun.com
 * @date Created in 2019/9/18 9:43
 */
@Slf4j
public class AutoCodeUtils {

    //获取配置实例
    private static AutoCodeProperties autoCodeProperties=ConstantFactory.getAutoCodeProperties();

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        if(ToolUtil.isEmpty(autoCodeProperties.getVmUrl())){
            templates.add("vmTemplate/Entity.java.vm");
            templates.add("vmTemplate/Dto.java.vm");
            templates.add("vmTemplate/Dao.java.vm");
            templates.add("vmTemplate/Dao.xml.vm");
            templates.add("vmTemplate/Service.java.vm");
            templates.add("vmTemplate/ServiceImpl.java.vm");
            templates.add("vmTemplate/Controller.java.vm");
            templates.add("vmTemplate/menu.sql.vm");
            templates.add("vmTemplate/index.vue.vm");
            templates.add("vmTemplate/add-or-update.vue.vm");
        }else {
            Resource resource=new ClassPathResource(autoCodeProperties.getVmUrl());
            File file= null;
            try {
                file = resource.getFile();
                File[] files=file.listFiles();
                for(File f:files) {
                    int mark= f.getAbsolutePath().lastIndexOf(File.separator);
                    String path=autoCodeProperties.getVmUrl()+f.getAbsolutePath().substring(mark);
                    templates.add(path);
                    log.info(path);
                }
            } catch (IOException e) {
                e.printStackTrace();

            }

        }


        return templates;
    }

    /**
     * 生成代码
     */
    public static boolean generatorCode(Map<String, String> table,
                                        List<Map<String, String>> columns, Map<String, String> params) {
        log.info("获取配置对象");
        log.info(JSON.toJSONString(ConstantFactory.getAutoCodeProperties()));
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), autoCodeProperties.getTablePrefix());
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class" , "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //String mainPath = config.getString("mainPath");
        //mainPath = StringUtils.isBlank(mainPath) ? "com.sonake" : mainPath;
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName" , tableEntity.getTableName());
        map.put("comments" , tableEntity.getComments());
        map.put("pk" , tableEntity.getPk());
        map.put("className" , tableEntity.getClassName());
        map.put("classname" , tableEntity.getClassname());
        map.put("pathName" , tableEntity.getClassname().toLowerCase());
        map.put("columns" , tableEntity.getColumns());
        map.put("hasBigDecimal" , hasBigDecimal);
        //map.put("mainPath" , mainPath);
        map.put("package" , autoCodeProperties.getGroupId());
        map.put("moduleName" , autoCodeProperties.getArtifactId());
        map.put("author" , autoCodeProperties.getAuthor());
        map.put("email" , autoCodeProperties.getEmail());
        map.put("datetime" , DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        int size = 0;
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                String fileName = getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"), params);
                //源文件存在先删除
                FileUtils.deleteFile(fileName);
                // 创建并写入文件
                if (FileUtils.createFile(fileName)) {
                    FileUtils.writeToFile(fileName, sw.toString(), true);
                }
                //zip.putNextEntry(new ZipEntry(fileName));
                //IOUtils.write(sw.toString(), zip, "UTF-8" );
                //IOUtils.closeQuietly(sw);
                //zip.closeEntry();
                size++;
            } catch (Exception e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
        if (templates.size() == size) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_" , "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，" , e);
        }
    }

//    /**
//     * 获取文件名
//     */
//    public static String getFileName(String vmTemplate, String className, String packageName, String moduleName,Map<String,String> params) {
//        String codeUrl = params.get("codeUrl").replace("/",File.separator).replace("\\",File.separator);
//        String xmlUrl = params.get("xmlUrl").replace("/",File.separator).replace("\\",File.separator);
//        String vueUrl = params.get("vueUrl").replace("/",File.separator).replace("\\",File.separator);
//        String packagePath =codeUrl+File.separator+"src"+File.separator+ "main" + File.separator + "java" + File.separator;
//        if (StringUtils.isNotBlank(packageName)) {
//            packagePath +=packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
//        }
//
//        if (vmTemplate.contains("Entity.java.vm" )) {
//            return packagePath + "entity" + File.separator + className + "Entity.java";
//        }
//        if(vmTemplate.contains("Dto.java.vm")){
//            return packagePath + "dto" + File.separator + className + "Dto.java";
//        }
//
//        if (vmTemplate.contains("dao.java.vm" )) {
//            return packagePath + "dao" + File.separator + className + "dao.java";
//        }
//
//        if (vmTemplate.contains("Service.java.vm" )) {
//            return packagePath + "service" + File.separator + className + "Service.java";
//        }
//
//        if (vmTemplate.contains("ServiceImpl.java.vm" )) {
//            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
//        }
//
//        if (vmTemplate.contains("Controller.java.vm" )) {
//            return packagePath + "controller" + File.separator + className + "Controller.java";
//        }
//
//        if (vmTemplate.contains("dao.xml.vm" )) {
//            return xmlUrl+File.separator+"src"+File.separator+"main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "dao.xml";
//        }
//
//        if (vmTemplate.contains("menu.sql.vm" )) {
//            return xmlUrl+File.separator+"src"+File.separator+"main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator+className.toLowerCase() + "_menu.sql";
//        }
//
//        if (vmTemplate.contains("index.vue.vm" )) {
//            return vueUrl + File.separator + className.toLowerCase() + ".vue";
//        }
//
//        if (vmTemplate.contains("add-or-update.vue.vm" )) {
//            return vueUrl +File.separator+ className.toLowerCase() + "-add-or-update.vue";
//        }
//
//
//        return null;
//    }


    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName, Map<String, String> params) {
        String codeUrl = params.get("codeUrl").replace("/" , File.separator).replace("\\" , File.separator);
        String xmlUrl = params.get("xmlUrl").replace("/" , File.separator).replace("\\" , File.separator);
        String vueUrl = params.get("vueUrl").replace("/" , File.separator).replace("\\" , File.separator);
        String packagePath = codeUrl + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace("." , File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("Entity.java.vm")) {
            return codeUrl + File.separator + className + ".java";
        }
        if (template.contains("Dto.java.vm")) {
            return codeUrl + File.separator + className + "Dto.java";
        }

        if (template.contains("Dao.java.vm")) {
            return codeUrl + File.separator + className + "dao.java";
        }

        if (template.contains("Service.java.vm")) {
            return codeUrl + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return codeUrl + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return codeUrl + File.separator + className + "Controller.java";
        }

        if (template.contains("Dao.xml.vm")) {
            return xmlUrl + File.separator + className + "dao.xml";
        }

        if (template.contains("menu.sql.vm")) {
            return xmlUrl + File.separator + className.toLowerCase() + "_menu.sql";
        }

        if (template.contains("index.vue.vm")) {
            return vueUrl + File.separator + className.toLowerCase() + ".vue";
        }

        if (template.contains("add-or-update.vue.vm")) {
            return vueUrl + File.separator + className.toLowerCase() + "-add-or-update.vue";
        }


        return null;
    }
}
