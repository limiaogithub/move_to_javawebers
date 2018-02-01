package com.github.yt.mybatis.config.fields;


import org.springframework.stereotype.Service;


@Service("defaultFieldsDefault")
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
