package com.github.yt.generator;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JavaCodeGenerator {

    private static final String BEAN = "bean";
    private static final String MAPPER = "mapper";
    private static final String MAPPER_XML = "mapper_xml";
    private static final String SERVICE = "service";
    private static final String CONTROLLER = "controller";
    private static final String HTML = "html";

    private String username;
    private String passWord;
    private String dbInstance;
    private String url;

    public JavaCodeGenerator(String username, String passWord, String dbInstance, String url) {
        this.username = username;
        this.passWord = passWord;
        this.dbInstance = dbInstance;
        this.url = url;
    }

    private static List<String> CLASS_NAME_SUFFIX_LIST = new ArrayList<>();

    static {
        CLASS_NAME_SUFFIX_LIST.add("T");
        CLASS_NAME_SUFFIX_LIST.add("R");
    }

    public String getReplaceSuffixClassName(String className) {
        if (className == null || className.length() < 1) {
            return className;
        }
        String suffix = className.substring(className.length() - 1, className.length());
        if (CLASS_NAME_SUFFIX_LIST.contains(suffix)) {
            className = className.substring(0, className.length() - 1);
        }
        return className;
    }

    /**
     * @param tableName     表名
     * @param codeName      表名对应的中文注释
     * @param modulePackage 模块包：com.fdcz.pro.system
     * @param moduleName    模块名
     * @param templates     生成那些模板
     */
    public void create(String tableName, String codeName, String moduleName, String modulePackage, String... templates) {
        if (null == tableName || "".equals(tableName)) {
            return;
        }
        if (null == codeName || "".equals(codeName)) {
            return;
        }
        if (null == modulePackage || "".equals(modulePackage)) {
            return;
        }

        CreateBean createBean = new CreateBean();
        createBean.setMysqlInfo(url, username, passWord, dbInstance);
        String className = createBean.getTablesNameToClassName(tableName);
        String replaceSuffixClassName = getReplaceSuffixClassName(className);
        String lowerName = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
        String replaceSuffixLowerName = replaceSuffixClassName.substring(0, 1).toLowerCase() + replaceSuffixClassName.substring(1, replaceSuffixClassName.length());
        String rootPath = CommonPageParser.getRootPath();
        String resourcePath = File.separator + "src" + File.separator + "main" + File.separator
                + "resources" + File.separator;
        String javaPath = File.separator + "src" + File.separator + "main" + File.separator + "java"
                + File.separator;
        String webappPath = File.separator + "src" + File.separator + "main" + File.separator + "webapp"
                + File.separator + "module" + File.separator;
        String moduleSimplePackage = modulePackage
                .substring(modulePackage.lastIndexOf(".") + 1, modulePackage.length());

        // //根路径
        // String srcPath = rootPath + "src" + File.separator + "main" + File.separator + "java";
        // //包路径
        // String pckPath = rootPath + "com" + File.separator + "fdcz" + File.separator + "pro" + File.separator +
        // "system";
        //
        // File file=new File(pckPath);
        // java,xml文件名称
        String modelPath = File.separator + "domain" + File.separator + className + ".java";
        String searchFormPath = File.separator + "controller" + File.separator + "form" + File.separator + className
                + "SearchForm.java";

        //String mapperPath = File.separator + "dao" + File.separator + className + "Mapper.java";
        String mapperPath = File.separator + "dao" + File.separator + replaceSuffixClassName + "Mapper.java";

        //String mapperXmlPath = File.separator + "dao" + File.separator + className + "Mapper.xml";
        String mapperXmlPath = File.separator + "dao" + File.separator + replaceSuffixClassName + "Mapper.xml";

        //String servicePath = File.separator + "service" + File.separator + className + "Service.java";
        String servicePath = File.separator + "service" + File.separator + replaceSuffixClassName + "Service.java";

        //String serviceImplPath = File.separator + "service" + File.separator + "impl" + File.separator + className
        // + "ServiceImpl.java";
        String serviceImplPath = File.separator + "service" + File.separator + "impl" + File.separator + replaceSuffixClassName
                + "ServiceImpl.java";
        //String controllerPath = File.separator + "controller" + File.separator + className + "Controller.java";
        String controllerPath = File.separator + "controller" + File.separator + replaceSuffixClassName + "Controller.java";

        //String sqlMapperPath = File.separator + "dao" + File.separator + className + "Mapper.xml";
        String sqlMapperPath = File.separator + "dao" + File.separator + replaceSuffixClassName + "Mapper.xml";

        //String htmlPath = File.separator + className + ".html";
        String htmlPath = File.separator + replaceSuffixLowerName + ".html";

        //String listJSPPath = lowerName + File.separator + className + "List.jsp";
        String listJSPPath = lowerName + File.separator + replaceSuffixLowerName + "List.jsp";

        // String editJSPPath = lowerName + File.separator + "edit" + className + ".jsp";
        String editJSPPath = lowerName + File.separator + "edit" + replaceSuffixLowerName + ".jsp";

        // String springPath="conf" + File.separator + "spring" + File.separator ;
        // String sqlMapPath="conf" + File.separator + "mybatis" + File.separator ;

        VelocityContext context = new VelocityContext();
        context.put("className", className);
        context.put("lowerName", lowerName);
        context.put("codeName", codeName);
        context.put("moduleName", moduleName);
        context.put("tableName", tableName);
        context.put("modulePackage", modulePackage);
        context.put("importPackage", modulePackage);
        context.put("moduleSimplePackage", moduleSimplePackage);
        context.put("replaceSuffixClassName", replaceSuffixClassName);
        context.put("replaceSuffixLowerName", replaceSuffixLowerName);
        /****************************** 生成bean字段 *********************************/
        try {
            context.put("feilds", createBean.getBeanFeilds(tableName)); // 生成bean
        } catch (Exception e) {
            e.printStackTrace();
        }

        /******************************* 生成sql语句 **********************************/
        try {
            Map<String, Object> sqlMap = createBean.getAutoCreateSql(tableName);
            List<ColumnData> columnDatas = createBean.getColumnDatas(tableName);
            List<ColumnData> normalColumns = new ArrayList<>();
            ColumnData columnDataPriKey = new ColumnData();
            for (ColumnData columnData : columnDatas) {
                if (columnData.getIsPriKey()) {
                    columnDataPriKey = columnData;
                    continue;
                }
                normalColumns.add(columnData);
            }
            context.put("columnDatas", columnDatas); // 生成bean
            context.put("prikey", columnDataPriKey.getColumnName()); // 生成主见
            context.put("normalColumns", normalColumns); // 生成非主键列表
            context.put("SQL", sqlMap);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // -------------------生成文件代码---------------------/
        String realPath = rootPath;
        String modulePakPath = modulePackage.replaceAll("\\.", "/");
        // 生成Model
        if (ArrayUtils.isNotEmpty(templates) && ArrayUtils.contains(templates, BEAN)) {
            CommonPageParser.WriterPage(context, "Bean.java.vm", realPath + javaPath + modulePakPath, modelPath); //
        }

        if (ArrayUtils.isNotEmpty(templates) && ArrayUtils.contains(templates, MAPPER)) {
            CommonPageParser.WriterPage(context, "Mapper.java.vm", realPath + javaPath + modulePakPath, mapperPath); // 生成MybatisMapper接口
        }

        if (ArrayUtils.isNotEmpty(templates) && ArrayUtils.contains(templates, MAPPER_XML)) {//生成xml
            CommonPageParser.WriterPage(context, "Mapper.xml.vm", realPath + resourcePath + modulePakPath, mapperXmlPath); //
            // 生成MybatisMapper接口
        }
        if (ArrayUtils.isNotEmpty(templates) && ArrayUtils.contains(templates, SERVICE)) {
            CommonPageParser.WriterPage(context, "Service.java.vm", realPath + javaPath + modulePakPath, servicePath);// 生成Service
            CommonPageParser.WriterPage(context, "ServiceImpl.java.vm", realPath + javaPath + modulePakPath, serviceImplPath);// 生成Service
        }

//      配置controller
        if (ArrayUtils.isNotEmpty(templates) && ArrayUtils.contains(templates, CONTROLLER)) {
            CommonPageParser.WriterPage(context, "Controller.java.vm", rootPath + javaPath + modulePakPath, controllerPath);// 生成Controller
        }
//      配置html
        if (ArrayUtils.isNotEmpty(templates) && ArrayUtils.contains(templates, HTML)) {
            CommonPageParser.WriterPage(context, "Html.html.vm", rootPath + webappPath + StringUtils.substringAfterLast
                    (modulePackage, "."), htmlPath);//
            // 生成Controller
        }
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getDbInstance() {
        return dbInstance;
    }

    public void setDbInstance(String dbInstance) {
        this.dbInstance = dbInstance;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
