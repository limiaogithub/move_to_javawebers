package com.github.yt.mybatis.config.fields;


import com.github.yt.mybatis.utils.SpringContextUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class FieldsConfiguration {

    private static FieldsDefault fieldsDefault;

    public static FieldsDefault create() {
        if (fieldsDefault != null) {
            return fieldsDefault;
        }
        Object object = null;
        boolean error = false;
        try {
            object = SpringContextUtils.getBean("ytFieldsConfig");
        } catch (NoSuchBeanDefinitionException e) {
            error = true;
        }
        if (error || object == null) {
            fieldsDefault = new DefaultFieldsDefault();
        } else {
            fieldsDefault = (FieldsDefault) object;
        }
        return fieldsDefault;
    }

}
