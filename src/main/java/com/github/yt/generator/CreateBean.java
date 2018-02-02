package com.github.yt.generator;

import org.apache.commons.lang.StringUtils;

import java.sql.*;
import java.util.*;


public class CreateBean {

    static String url;
    static String username;
    static String password;
    static String dbInstance;

    private static final List ingoreColumns = Arrays.asList("founderId", "founderName", "modifierId",
            "modifierName", "deleteFlag", "createDateTime", "modifyDateTime");

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMysqlInfo(String url, String username, String password, String dbInstance) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.dbInstance = dbInstance;
    }

    public Connection getConnection() throws SQLException {
        System.out.println(url);
        return DriverManager.getConnection(url, username, password);
    }

    public List<ColumnData> getColumnDatas(String tableName) throws SQLException {
        String SQLColumns = "SELECT distinct COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT,COLUMN_KEY,CHARACTER_MAXIMUM_LENGTH" +
                ",IS_NULLABLE,COLUMN_DEFAULT  FROM information_schema.columns WHERE table_name =  '" + tableName + "' " + "and table_schema='" + dbInstance + "' ";
        Connection con = this.getConnection();
        PreparedStatement ps = con.prepareStatement(SQLColumns);
        List<ColumnData> columnList = new ArrayList<ColumnData>();
        ResultSet rs = ps.executeQuery();
        StringBuffer str = new StringBuffer();
        StringBuffer getset = new StringBuffer();
        while (rs.next()) {
            String name = rs.getString(1);
            String type = rs.getString(2);
            String comment = rs.getString(3);
            String priKey = rs.getString(4);
            Long length = rs.getLong(5);
            String isNullable = rs.getString(6);
            String columnDefault = rs.getString(7);
            type = this.getType(type);
            if (ingoreColumns.contains(name)) {
                continue;
            }
            ColumnData cd = new ColumnData();
            cd.setColumnName(name);
            cd.setDataType(type);
            cd.setColumnComment(comment);
            cd.setColumnNameContainEntity("${entity." + name + " }");
            cd.setIsPriKey("PRI".equals(priKey));
            cd.setColumnLength(length);
            cd.setIsNullable("NO".equals(isNullable));
            cd.setColumnDefault(columnDefault);
            columnList.add(cd);
        }
        argv = str.toString();
        method = getset.toString();
        rs.close();
        ps.close();
        con.close();
        return columnList;
    }

    private String method;
    private String argv;


    public String getBeanFeilds(String tableName) throws SQLException {
        List<ColumnData> dataList = getColumnDatas(tableName);
        StringBuffer str = new StringBuffer();
        StringBuffer getset = new StringBuffer();
        for (ColumnData d : dataList) {
            String name = getTablesColumnToAttributeName(d.getColumnName());
            String type = d.getDataType();
            String comment = d.getColumnComment();
            Long length = d.getColumnLength();
            Boolean isNullAble = d.getIsNullable();
            String columnDefault = d.getColumnDefault();
            // type=this.getType(type);
            String maxChar = name.substring(0, 1).toUpperCase();
            str.append("\r\n\t/** \r\n\t * ").append(comment).append("  \r\n\t */");
            if (d.getIsPriKey()) {
                str.append("\r\n\t@javax.persistence.Id");
            }
            if (StringUtils.isEmpty(columnDefault) && isNullAble != null && !isNullAble) {
                str.append("\r\n\t@NotEmpty(message = \"" + name + "不能为空！\")");
            }
            if (length != null && length > 0) {
                str.append("\r\n\t@Length(max = " + length + ", message = \"" + name + "长度不能超过" + length + "！\")");
            }
            str.append("\r\n\t").append("private ").append(type + " ").append(name).append(";");
            String method = maxChar + name.substring(1, name.length());
            getset.append("\r\n\t\r\n\t").append("public ").append(type + " ").append("get" + method + "() {\r\n\t");
            getset.append("    return this.").append(name).append(";\r\n\t}");
            getset.append("\r\n\t\r\n\t").append("public ").append(getTablesNameToClassName(tableName)).append(" ")
                    .append("set" + method + "(" + type + " " + name + ") {\r\n\t");
            getset.append("\tthis.").append(name).append(" = ").append(name).append(";\r\n\t\treturn this;\r\n\t}");
        }
        argv = str.toString();
        method = getset.toString();
        return argv + method;
    }

    public String getType(String type) {
        switch (type = type.toLowerCase()) {
            case "char":
            case "varchar":
            case "text":
                return "String";
            case "int":
                return "Integer";
            case "bigint":
                return "java.math.BigInteger";
            case "decimal":
                return "java.math.BigDecimal";
            case "timestamp":
            case "date":
            case "datetime":
                // return "java.sql.Timestamp";
                return "java.util.Date";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "tinyint":
                return "Boolean";
            default:
                return null;
        }
    }

    public String getTablesNameToClassName(String tableName) {
        String[] split = tableName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = split[i].substring(0, 1).toUpperCase()
                        + split[i].substring(1).toLowerCase();
                sb.append(tempTableName);
            }
            System.out.println(sb.toString());
            return sb.toString();
        } else {
            String tempTables = split[0].substring(0, 1).toUpperCase() + split[0].substring(1, split[0].length());
            return tempTables;
        }
    }


    public String getTablesColumnToAttributeName(String columnName) {
        String[] split = columnName.split("_");
        if (split.length > 1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                String tempTableName = "";
                if (i == 0) {
                    tempTableName = split[i].substring(0, 1).toLowerCase() + split[i].substring(1, split[i].length());
                } else {
                    tempTableName = split[i].substring(0, 1).toUpperCase() + split[i].substring(1, split[i].length());
                }
                sb.append(tempTableName);
            }
            System.out.println(sb.toString());
            return sb.toString();
        } else {
            String tempTables = split[0].substring(0, 1).toLowerCase() + split[0].substring(1, split[0].length());
            return tempTables;
        }
    }

    public Map<String, Object> getAutoCreateSql(String tableName) throws Exception {
        Map<String, Object> sqlMap = new HashMap<String, Object>();
        List<ColumnData> columnDatas = getColumnDatas(tableName);
        String columns = this.getColumnSplit(columnDatas);
        String[] columnList = getColumnList(columns);
        String columnFields = getColumnFields(columns);
        sqlMap.put("columnList", columnList);
        sqlMap.put("columnFields", columnFields);
        return sqlMap;
    }

    public String getColumnFields(String columns) throws SQLException {
        String fields = columns;
        if (fields != null && !"".equals(fields)) {
            fields = fields.replaceAll("[|]", ",");
        }
        return fields;
    }

    public String[] getColumnList(String columns) throws SQLException {
        String[] columnList = columns.split("[|]");
        return columnList;
    }

    public String getColumnSplit(List<ColumnData> columnList) throws SQLException {
        StringBuffer commonColumns = new StringBuffer();
        for (ColumnData data : columnList) {
            commonColumns.append(data.getColumnName() + "|");
        }
        return commonColumns.delete(commonColumns.length() - 1, commonColumns.length()).toString();
    }

}
