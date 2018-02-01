package com.github.yt.mybatis.config.fields;

/**
 * domain默认值注入接口
 */
public interface FieldsDefault {

    /**
     * 获取操作人
     *
     * @return 操作人
     */
    String getOperator();

    /**
     * 获取操作人id
     *
     * @return 操作人id
     */
    String getOperatorId();

    /**
     * 获取修改人
     *
     * @return 修改人
     */
    String getModifyOperator();

    /**
     * 获取修改人id
     *
     * @return 修改人id
     */
    String getModifyOperatorId();
}
