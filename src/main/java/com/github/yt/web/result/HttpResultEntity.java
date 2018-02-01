package com.github.yt.web.result;

public class HttpResultEntity<T> {

    private String errorCode;

    private String message;

    private Object result;

    public HttpResultEntity() {
    }

    public HttpResultEntity(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public HttpResultEntity(String errorCode, String message, Object result) {
        this.errorCode = errorCode;
        this.message = message;
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
