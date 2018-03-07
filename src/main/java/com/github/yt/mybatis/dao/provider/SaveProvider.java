package com.github.yt.mybatis.dao.provider;


import com.github.yt.base.exception.BaseErrorException;
import com.github.yt.mybatis.dao.BaseMapper;
import com.github.yt.mybatis.dao.MapperProvider;
import com.github.yt.mybatis.domain.BaseEntity;
import com.github.yt.mybatis.utils.JPAUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.github.yt.mybatis.mybatis.SqlBuilder.*;


public class SaveProvider extends MapperProvider {

    public String save(Map<String, Object> param) {
        Class<?> entityClass = param.get(BaseMapper.ENTITY).getClass();
        begin();
        INSERT_INTO(getTableName(entityClass));
        Field idField = null;
        for (Field field : JPAUtils.getAllFields(entityClass)) {
            field.setAccessible(true);
            if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                idField = field;
                continue;
            }
            String fieldName = JPAUtils.getAnnotationColumnName(field);
            Object value = JPAUtils.getValue(param.get(BaseMapper.ENTITY), field.getName());
            if (null == value) {
                continue;
            }
            VALUES(fieldName, StringUtils.join("#{", BaseMapper.ENTITY, ".", field.getName(), "}"));
        }
        if (null == idField) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), "实体未配置@Id "));
        }
        setId(param, idField);
        return sql();
    }

    public String saveBatch(Map<String, Object> param) {
        if (null == param.get(BaseMapper.ENTITIES)) {
            throw new BaseErrorException("批量插入的数据为null");
        }
        Class<?> entityClass = ((List) param.get(BaseMapper.ENTITIES)).get(0).getClass();
        begin();
        BATCH_INSERT_INTO(getTableName(entityClass));
        Field idField = null;
        for (int i = 0; i < ((List) param.get(BaseMapper.ENTITIES)).size(); i++) {
            for (Field field : JPAUtils.getAllFields(entityClass)) {
                field.setAccessible(true);
                if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                    idField = field;
                    continue;
                }
                String fieldName = JPAUtils.getAnnotationColumnName(field);
                Object value = JPAUtils.getValue(((List) param.get(BaseMapper.ENTITIES)).get(i), field.getName());
                if (null == value) {
                    continue;
                }
                BATCH_VALUES(fieldName, StringUtils.join("#{", BaseMapper.ENTITIES, "[", i, "]", ".", field.getName(), "}"));
            }
            if (null == idField) {
                throw new BaseErrorException(StringUtils.join(entityClass.getName(), "实体未配置@Id "));
            }
            setIdBatch((BaseEntity) ((List) param.get(BaseMapper.ENTITIES)).get(i), i, idField);
            BATCH_SEGMENTATION();
        }
        return sql();
    }

    public String saveForSelective(Map<String, Object> param) {
        Class<?> entityClass = param.get(BaseMapper.ENTITY).getClass();
        begin();
        INSERT_INTO(getTableName(entityClass));
        Field idField = null;
        for (Field field : JPAUtils.getAllFields(entityClass)) {
            field.setAccessible(true);
            if (null != field.getAnnotation(Id.class) || null != field.getAnnotation(Transient.class)) {
                idField = field;
                continue;
            }
            try {
                String fieldName = JPAUtils.getAnnotationColumnName(field);
                if (field.get(param.get(BaseMapper.ENTITY)) != null) {
                    VALUES(fieldName, StringUtils.join("#{", BaseMapper.ENTITY, ".", field.getName(), "}"));
                }
            } catch (IllegalAccessException e) {
                throw new BaseErrorException(e);
            }
        }
        if (null == idField) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), "实体未配置@Id "));
        }
        setId(param, idField);
        return sql();
    }

    private void setId(Map<String, Object> param, Field idField) {
        if (!idField.getType().isAssignableFrom(String.class)) {
            return;
        }
        try {
            String fieldName = JPAUtils.getAnnotationColumnName(idField);
            if (StringUtils.isNotEmpty((String) idField.get(param.get(BaseMapper.ENTITY)))) {
                VALUES(fieldName, StringUtils.join("#{", BaseMapper.ENTITY, ".", idField.getName(), "}"));
                return;
            }
            String id = UUID.randomUUID().toString().replace("-", "");
            VALUES(fieldName, StringUtils.join("'", id, "'"));
            idField.set(param.get(BaseMapper.ENTITY), id);
        } catch (IllegalAccessException e) {
            throw new BaseErrorException(e);
        }
    }

    private void setIdBatch(BaseEntity baseEntity, int i, Field idField) {
        if (!idField.getType().isAssignableFrom(String.class)) {
            return;
        }
        try {
            String fieldName = JPAUtils.getAnnotationColumnName(idField);
            if (StringUtils.isNotEmpty((String) idField.get(baseEntity))) {
                BATCH_VALUES(fieldName, StringUtils.join("#{", BaseMapper.ENTITIES, "[", i, "]", ".", idField.getName(), "}"));
                return;
            }
            String id = UUID.randomUUID().toString().replace("-", "");
            BATCH_VALUES(fieldName, StringUtils.join("'", id, "'"));
            idField.set(baseEntity, id);
        } catch (IllegalAccessException e) {
            throw new BaseErrorException(e);
        }
    }

}
