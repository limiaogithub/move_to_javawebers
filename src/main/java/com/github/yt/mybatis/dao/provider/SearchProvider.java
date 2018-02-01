package com.github.yt.mybatis.dao.provider;


import com.github.yt.mybatis.dao.BaseMapper;
import com.github.yt.mybatis.dao.MapperProvider;
import com.github.yt.mybatis.exception.BaseErrorException;
import com.github.yt.mybatis.utils.JPAUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.yt.mybatis.mybatis.SqlBuilder.*;


public class SearchProvider extends MapperProvider {

    public String findById(Map<String, Object> param) {
        begin();
        Class<?> entityClass = (Class) param.get(BaseMapper.ENTITY_CLASS);
        if (param.get(BaseMapper.ID) == null || StringUtils.isEmpty(param.get(BaseMapper.ID).toString())) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), ",find单个对象时主键不能为空!"));
        }
        SELECT("*");
        FROM(getTableName(entityClass));
        WHERE(getEqualsValue(JPAUtils.getIdField(entityClass).getName(), BaseMapper.ID));
        return sql();
    }

    public String findAll(Map<String, Object> param) {
        begin();
        Class<?> entityClass = (Class) param.get(BaseMapper.ENTITY_CLASS);
        Map<String, Object> map = ((Map<String, Object>) param.get(BaseMapper.DATA));
        String selectColumnSql = createSelectColumnSql(map);
        if (MapUtils.isNotEmpty(map) && map.containsKey("distinct")) {
            SELECT_DISTINCT(selectColumnSql);
        } else {
            SELECT(selectColumnSql);
        }
        FROM(getTableNameWithAlias(entityClass));
        createAllWhere(entityClass, map, false);
        return StringUtils.join(sql(), createLimit(map));
    }

    public String pageTotalRecord(Map<String, Object> param) {
        begin();
        Class<?> entityClass = (Class) param.get(BaseMapper.ENTITY_CLASS);
        SELECT(createSelectCountColumnSql(param));
        FROM(getTableNameWithAlias(entityClass));
        createAllWhere(entityClass, (Map<String, Object>) param.get(BaseMapper.DATA), true);
        return sql();
    }

    private void createAllWhere(Class<?> entityClass, Map<String, Object> param, boolean isCount) {
        if (MapUtils.isEmpty(param)) {
            return;
        }
        try {
            for (Field field : JPAUtils.getAllFields(entityClass)) {
                createFieldWhereSql(field, param);
            }
            parseQueryHandle(param, isCount);
        } catch (Exception e) {
            throw new BaseErrorException(e);
        }
    }

    private boolean createFieldWhereSql(Field field, Map<String, Object> param) {
        if (!validateFieldWhereSql(field, param)) {
            return false;
        }
        if (null != field.getType().getAnnotation(Table.class)) {
            return false;
        }
        WHERE(StringUtils.join("t.", getEqualsValue(field.getName(), StringUtils.join(BaseMapper.DATA + ".", field.getName()))));
        return true;
    }

    private boolean validateFieldWhereSql(Field field, Map<String, Object>
            param) {
        if (null != field.getAnnotation(Transient.class) || field.getType().isAssignableFrom(Class.class)) {
            return false;
        }
        return param.containsKey(field.getName());
    }

    private void parseQueryHandle(Map<String, Object> param, boolean isCount) {
        if (param.containsKey("orderBy") && !isCount) {
            LinkedHashMap<String, String> orderByMap = (LinkedHashMap<String, String>) param.get("orderBy");
            for (String orderBy : orderByMap.keySet()) {
                if (orderBy.contains(".")) {
                    ORDER_BY(orderBy + " " + orderByMap.get(orderBy));
                    continue;
                }
                ORDER_BY(StringUtils.join("t.", orderBy, " ", orderByMap.get(orderBy)));
            }
        }
        if (param.containsKey("whereSqls")) {
            List<String> whereSqlList = (List<String>) param.get("whereSqls");
            for (String whereSql : whereSqlList) {
                WHERE(whereSql);
            }
        }
    }

    private String createLimit(Map<String, Object> param) {
        return !param.containsKey(BaseMapper.START) ? "" : StringUtils.join(" limit ", (Integer) param.get(BaseMapper.START), " , ", (Integer) param.get(BaseMapper.LIMIT));
    }

    private String createSelectCountColumnSql(Map<String, Object> param) {
        Map<String, Object> map = ((Map<String, Object>) param.get(BaseMapper.DATA));
        String selectColumnSql = "count(distinct t." + JPAUtils.getIdField((Class<?>) param.get(BaseMapper.ENTITY_CLASS)).getName() + ")";
        if (map == null) {
            return selectColumnSql;
        }
        if (map.containsKey("selectColumnSql") && map.get("selectColumnSql") != null) {
            selectColumnSql = " count(distinct " + map.get("selectColumnSql").toString() + ") ";
        }
        return selectColumnSql;
    }

    private String createSelectColumnSql(Map<String, Object> map) {
        String selectColumnSql = " t.* ";
        if (MapUtils.isNotEmpty(map) && map.containsKey("selectColumnSql") && map.get("selectColumnSql") != null) {
            selectColumnSql = " " + map.get("selectColumnSql").toString() + " ";
        }
        return selectColumnSql;
    }
}
