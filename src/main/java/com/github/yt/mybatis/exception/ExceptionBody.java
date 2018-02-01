package com.github.yt.mybatis.exception;


import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

public class ExceptionBody {

    public static String MESSAGE = "message";
    private String code;
    private String message;

    public ExceptionBody(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ExceptionBody(Enum errorEnum) {
        this.code = errorEnum.name();
        try {
            this.message = PropertyUtils.getProperty(errorEnum, MESSAGE).toString();
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var3) {
            throw new BaseErrorException(this.message, var3);
        }
    }

    public String getCode() {
        return this.code;
    }

    public ExceptionBody setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public ExceptionBody setMessage(String message) {
        this.message = message;
        return this;
    }

    public String toString() {
        return "errorCode:" + this.code + " errorMessage:" + this.message;
    }
}
