package com.github.yt.mybatis.dao.provider;


import com.github.yt.mybatis.dao.BaseMapper;
import com.github.yt.mybatis.dao.MapperProvider;
import com.github.yt.base.exception.BaseErrorException;
import com.github.yt.mybatis.utils.JPAUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static com.github.yt.mybatis.mybatis.SqlBuilder.*;


public class ModifyProvider extends MapperProvider {

    public String update(Map<String, Object> param) {
        Class<?> entityClass = param.get(BaseMapper.ENTITY).getClass();
        begin();
        UPDATE(getTableName(entityClass));
        Field idField = null;
        for (Field field : JPAUtils.getAllFields(entityClass)) {
            field.setAccessible(true);
            if (field.getType().isAssignableFrom(Collection.class) || null != field.getType().getAnnotation(Table.class)) {
                continue;
            }
            String fieldName = JPAUtils.getAnnotationColumnName(field);
            if (null == field.getAnnotation(Id.class)) {
                SET(StringUtils.join(getEqualsValue(fieldName, StringUtils.join(BaseMapper.ENTITY, ".", field.getName()))));
                continue;
            }
            idField = field;
            WHERE(getEqualsValue(fieldName, StringUtils.join(BaseMapper.ENTITY, ".", field.getName())));
        }
        if (null == idField) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), "实体未配置@Id "));
        }
        return sql();
    }

    public String updateForSelective(Map<String, Object> param) {
        Class<?> entityClass = param.get(BaseMapper.ENTITY).getClass();
        begin();
        UPDATE(getTableName(entityClass));
        Field idField = null;
        for (Field field : JPAUtils.getAllFields(entityClass)) {
            field.setAccessible(true);
            if (field.getType().isAssignableFrom(Collection.class) || null != field.getType().getAnnotation(Table.class)) {
                continue;
            }
            if (null != field.getAnnotation(Id.class)) {
                idField = field;
                continue;
            }
            Object value = JPAUtils.getValue(param.get(BaseMapper.ENTITY), field.getName());
            if (null == value) {
                continue;
            }
            SET(StringUtils.join(getEqualsValue(JPAUtils.getAnnotationColumnName(field), StringUtils.join(BaseMapper.ENTITY, ".", field.getName()))));
        }
        if (null == idField) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), "实体未配置@Id "));
        }
        WHERE(getEqualsValue(JPAUtils.getAnnotationColumnName(idField), StringUtils.join(BaseMapper.ENTITY, ".", idField.getName())));
        return sql();
    }

}
