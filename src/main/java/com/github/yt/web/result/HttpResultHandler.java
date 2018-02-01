package com.github.yt.web.result;

public class HttpResultHandler {

    public static HttpResultEntity<?> getSuccessResult() {
        return getSuccessResult((Object)null);
    }

    public static HttpResultEntity<?> getSuccessResult(Object result) {
        return new HttpResultEntity(HttpResultHandler.HttpResultEnum.SUCCESS.errorCode, HttpResultHandler.HttpResultEnum.SUCCESS.name(), result);
    }

    public static HttpResultEntity<?> getErrorResult() {
        return new HttpResultEntity(HttpResultHandler.HttpResultEnum.ERROR.errorCode, HttpResultHandler.HttpResultEnum.ERROR.name());
    }

    public static HttpResultEntity<?> getErrorResult(Object result) {
        return new HttpResultEntity(HttpResultHandler.HttpResultEnum.ERROR.errorCode, HttpResultHandler.HttpResultEnum.ERROR.name(), result);
    }

    public static HttpResultEntity<?> getErrorResult(String message) {
        return new HttpResultEntity(HttpResultEnum.ERROR.errorCode, message);
    }

    public static HttpResultEntity<?> getErrorResult(String errorCode, String message) {
        return new HttpResultEntity(errorCode, message);
    }

    enum HttpResultEnum {
        ERROR("0"),
        SUCCESS("1");
        private String errorCode;
        HttpResultEnum(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}
