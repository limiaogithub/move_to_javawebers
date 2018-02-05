package com.github.yt.mybatis.service.impl;

import com.github.yt.mybatis.config.fields.FieldsConfiguration;
import com.github.yt.mybatis.config.fields.FieldsDefault;
import com.github.yt.mybatis.dao.BaseMapper;
import com.github.yt.mybatis.domain.BaseEntity;
import com.github.yt.mybatis.handler.QueryHandler;
import com.github.yt.mybatis.result.QueryResult;
import com.github.yt.mybatis.service.BaseService;
import com.github.yt.mybatis.utils.JPAUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class ServiceSupport<T, M extends BaseMapper<T>> implements BaseService<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract M getMapper();

    @Override
    public void save(T entity) {
        this.processCreateColumns(entity);
        getMapper().save(entity);
    }

    @Override
    public void saveBatch(List<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        if (BaseEntity.class.isAssignableFrom(entities.get(0).getClass())) {
            for (T entity : entities) {
                this.processCreateColumns(entity);
            }
        }
        getMapper().saveBatch(entities);
    }

    @Override
    public void saveForSelective(T entity) {
        this.processCreateColumns(entity);
        getMapper().saveForSelective(entity);
    }

    @Override
    public void update(T entity) {
        if (null == JPAUtils.gtIdValue(entity)) {
            return;
        }
        processModifyColumns(entity);
        getMapper().update(entity);
    }

    @Override
    public void updateForSelective(T entity) {
        if (null == JPAUtils.gtIdValue(entity)) {
            return;
        }
        this.processModifyColumns(entity);
        getMapper().updateForSelective(entity);
    }

    @Override
    public void delete(Class<T> clazz, Serializable id) {
        getMapper().delete(clazz, id);
    }

    @Override
    public void logicDelete(Class<T> clazz, Serializable id) {
        getMapper().logicDelete(clazz, id);
    }

    @Override
    public T find(Class<T> clazz, Serializable id) {
        return getMapper().find(clazz, id);
    }

    @Override
    public List<T> findAll(T entity) {
        return getMapper().findAll(entity, new QueryHandler());
    }

    @Override
    public List<T> findAll(T entity, QueryHandler queryHandler) {
        return getMapper().findAll(entity, queryHandler == null ? new QueryHandler() : queryHandler);
    }

    @Override
    public QueryResult<T> getData(T entity, QueryHandler queryHandle) {
        QueryResult<T> qr = new QueryResult();
        if (queryHandle == null) {
            queryHandle = new QueryHandler();
        }
        qr.setRecordsTotal(getMapper().pageTotalRecord(entity, queryHandle));
        if (qr.getRecordsTotal() == 0) {
            qr.setData(new ArrayList<T>());
            return qr;
        }
        qr.setData(getMapper().findAll(entity, queryHandle));
        return qr;
    }

    private Object getSessionAttr(String attr) {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession()
                .getAttribute(attr);
    }

    private void setFounder(BaseEntity entity) {
        FieldsDefault fieldsDefault = FieldsConfiguration.create();
        try {
            if (StringUtils.isEmpty(entity.getFounderId())) {
                entity.setFounderId(fieldsDefault.getOperatorId());
            }
            if (StringUtils.isEmpty(entity.getFounderName())) {
                entity.setFounderName(fieldsDefault.getOperator());
            }
        } catch (Exception e) {
            logger.debug("setFounder时session获取失败!");
            entity.setFounderId(fieldsDefault.getOperatorId());
            entity.setFounderName(fieldsDefault.getOperator());
        }
    }

    private void setModifyFounder(BaseEntity entity) {
        FieldsDefault fieldsDefault = FieldsConfiguration.create();
        try {
            if (StringUtils.isEmpty(entity.getModifierId())) {
                entity.setFounderId(fieldsDefault.getModifyOperatorId());
            }
            if (StringUtils.isEmpty(entity.getModifierName())) {
                entity.setFounderName(fieldsDefault.getModifyOperator());
            }
        } catch (Exception e) {
            logger.info("update时session获取失败!");
            entity.setModifierId(fieldsDefault.getModifyOperatorId());
            entity.setModifierName(fieldsDefault.getModifyOperator());
        }
    }

    private void processCreateColumns(T entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = ((BaseEntity) entity);
            if (baseEntity.getCreateDateTime() == null) {
                baseEntity.setCreateDateTime(new Date());
            }
            if (baseEntity.getModifyDateTime() == null) {
                baseEntity.setModifyDateTime(baseEntity.getCreateDateTime());
            }
            setFounder((BaseEntity) entity);
        }
    }

    private void processModifyColumns(T entity) {
        if (entity instanceof BaseEntity) {
            BaseEntity baseEntity = ((BaseEntity) entity);
            if (baseEntity.getModifyDateTime() == null) {
                baseEntity.setModifyDateTime(new Date());
            }
            this.setModifyFounder((BaseEntity) entity);
        }
    }
}