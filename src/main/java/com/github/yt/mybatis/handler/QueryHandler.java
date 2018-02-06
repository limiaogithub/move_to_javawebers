package com.github.yt.mybatis.handler;

import com.github.yt.mybatis.config.page.PageConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class QueryHandler {

    //是否使用distinct
    private Boolean distinct;
    //扩展的whereSql
    private List<String> whereSqls = new ArrayList<>();

    private LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
    //扩展数据
    private Map<String, Object> expandData = new HashMap<>();

    private LinkedList<SQLJoinHandler> sqlJoinHandler = new LinkedList<>();

    private Integer start;

    private Integer limit;

    //指定查询列sql，例如“ t.name,t.pass”
    protected String selectColumnSql;

    public QueryHandler setSelectColumnSql(String sql) {
        if (StringUtils.isNotEmpty(sql)) {
            this.selectColumnSql = sql;
        }
        return this;
    }

    public QueryHandler addWhereSql(String whereSql) {
        if (StringUtils.isNotEmpty(whereSql)) {
            whereSqls.add(whereSql);
        }
        return this;
    }

    public QueryHandler addOrderBy(String key, String value) {
        orderBy.put(key, value);
        return this;
    }

    public QueryHandler addDistinct() {
        distinct = true;
        return this;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public QueryHandler addExpandData(String key, Object value) {
        expandData.put(key, value);
        return this;
    }

    public Map<String, Object> getExpandData() {
        return expandData;
    }

    public String getSelectColumnSql() {
        return selectColumnSql;
    }


    public Integer getStart() {
        return start;
    }

    public QueryHandler setStart(Integer start) {
        this.start = start;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public QueryHandler setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public QueryHandler configPage() {
        PageConfiguration.create().convert(this, getHttpServletRequest());
        return this;
    }

    public QueryHandler addJoinHandle(String selectColumns, SQLJoinHandler.JoinType joinType, String joinSql) {
        sqlJoinHandler.add(new SQLJoinHandler(selectColumns, joinType, joinSql));
        return this;
    }

    protected static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    public static String getInSql(String fieldName, int querySize) {
        if (querySize == 0) {
            return " (null) ";
        }
        String str = "";
        for (int i = 0; i < querySize; i++) {
            str = str + ",#{" + fieldName + "[" + i + "]}";
        }
        return "(" + str.replaceFirst(",", "") + ")";
    }
}

