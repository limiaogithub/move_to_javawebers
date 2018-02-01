package com.github.yt.mybatis.service;

import com.github.yt.mybatis.handler.QueryHandler;
import com.github.yt.mybatis.result.QueryResult;

import java.io.Serializable;
import java.util.List;

/**
 * 服务接口的基类
 *
 * @param <T>此服务接口服务的数据模型，即model
 */
public interface BaseService<T> {

    /**
     * 保存实体
     *
     * @param entity 待保存的实体
     */
    void save(T entity);

    /**
     * 批量保存
     *
     * @param entities 待保存实体列表
     */
    void saveBatch(List<T> entities);

    /**
     * 只保存非空字段
     *
     * @param entity 待保存的实体
     */
    void saveForSelective(T entity);

    /**
     * 更新实体
     *
     * @param entity 业务实体
     */
    void update(T entity);

    /**
     * 只更新非空字段
     *
     * @param entity 业务实体
     */
    void updateForSelective(T entity);

    /**
     * 删除实体
     *
     * @param clazz clazz
     * @param id    业务实体ID
     */
    void delete(Class<T> clazz, Serializable id);

    /**
     * 逻辑删除实体
     *
     * @param clazz clazz
     * @param id    业务实体ID
     */
    void logicDelete(Class<T> clazz, Serializable id);

    /**
     * 根据ID获取实体
     *
     * @param id 业务实体ID
     * @return 业务实体
     */
    T find(Class<T> clazz, Serializable id);

    /**
     * 按条件查询记录集合
     *
     * @param entity 业务实体类或业务查询实体类
     * @return 业务实体集合
     */
    List<T> findAll(T entity);

    /**
     * 按条件查询记录集合
     *
     * @param entity       业务实体类或业务查询实体类
     * @param queryHandler 查询辅助类
     * @return 业务实体集合
     */
    List<T> findAll(T entity, QueryHandler queryHandler);

    /**
     * 获取数据
     *
     * @param entity       查询业务实体
     * @param queryHandler 查询辅助类
     * @return 根据查询条件查询的查询结果集
     */
    QueryResult<T> getData(T entity, QueryHandler queryHandler);
}
