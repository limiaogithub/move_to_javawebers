package com.github.yt.mybatis.dao.provider;


import com.github.yt.mybatis.dao.BaseMapper;
import com.github.yt.mybatis.dao.MapperProvider;
import com.github.yt.mybatis.domain.BaseEntity;
import com.github.yt.mybatis.exception.BaseErrorException;
import com.github.yt.mybatis.utils.JPAUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.github.yt.mybatis.mybatis.SqlBuilder.*;


public class RemoveProvider extends MapperProvider {

    public String delete(Map<String, Object> param) {
        begin();
        Class<?> entityClass = (Class) param.get(BaseMapper.ENTITY_CLASS);
        if (param.get(BaseMapper.ID) == null || StringUtils.isEmpty(param.get(BaseMapper.ID).toString())) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), ",删除时主键不能为空!"));
        }
        DELETE_FROM(getTableName(entityClass));
        WHERE(getEqualsValue(JPAUtils.getIdField(entityClass).getName(), BaseMapper.ID));
        return sql();
    }

    public String logicDelete(Map<String, Object> param) {
        begin();
        Class<?> entityClass = (Class) param.get(BaseMapper.ENTITY_CLASS);
        if (param.get(BaseMapper.ID) == null || StringUtils.isEmpty(param.get(BaseMapper.ID).toString())) {
            throw new BaseErrorException(StringUtils.join(entityClass.getName(), ",删除时主键不能为空!"));
        }
        UPDATE(getTableName(entityClass));
        SET(BaseEntity.DELETE_FLAG + "=1");
        WHERE(getEqualsValue(JPAUtils.getIdField(entityClass).getName(), BaseMapper.ID));
        return sql();
    }

}
