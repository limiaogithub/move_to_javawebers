package com.github.yt.mybatis.config.fields;


public class DefaultFieldsDefault implements FieldsDefault {


    @Override
    public String getOperator() {
        return "default";
    }

    @Override
    public String getOperatorId() {
        return "";
    }

    @Override
    public String getModifyOperator() {
        return "default";
    }

    @Override
    public String getModifyOperatorId() {
        return "";
    }

}
