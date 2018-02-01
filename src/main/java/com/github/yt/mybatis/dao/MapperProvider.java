package com.github.yt.mybatis.dao;

import com.github.yt.mybatis.exception.BaseErrorException;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.lang.annotation.Annotation;

import static com.github.yt.mybatis.mybatis.SqlBuilder.BEGIN;
import static com.github.yt.mybatis.mybatis.SqlBuilder.SQL;


public class MapperProvider {

    protected void begin() {
        BEGIN();
    }

    protected String sql() {
        return SQL();
    }

    protected String getTableName(Class entityClass) {
        Annotation table = entityClass.getAnnotation(Table.class);
        if (null == table) {
            throw new BaseErrorException(StringUtils.join("实体未配置Table注解 entityClass =", entityClass.getName()));
        }
        String tableName = ((Table) table).name();
        if (StringUtils.isEmpty(tableName)) {
            throw new BaseErrorException(StringUtils.join("实体的Table注解未配置name属性 entityClass =", entityClass.getName()));
        }
        return tableName;
    }

    protected String getTableNameWithAlias(Class entityClass) {
        return StringUtils.join(getTableName(entityClass), " t");
    }

    protected String getEqualsValue(String column, String value) {
        return StringUtils.join(column, " = #{", value, "}");
    }

}
