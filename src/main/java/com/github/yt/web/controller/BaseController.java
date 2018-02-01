package com.github.yt.web.controller;

import com.github.yt.mybatis.exception.BaseAccidentException;
import com.github.yt.web.result.HttpResultEntity;
import com.github.yt.web.result.HttpResultHandler;
import com.github.yt.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

    @Resource
    protected HttpSession session;

    @ExceptionHandler
    public HttpResultEntity<?> exceptionHandler(HttpServletRequest request, Exception ex) {

        String logTemplate = "\n url:★{}★\n parameterValues : ★{}★";
        String parameterValues = WebUtils.getParameterValues(request);
        String serverName = request.getServerName();
        String operatorLogStr = "\n ★{User-Agent:[" + request.getHeader("User-Agent") + "],serverName:[" + serverName + "]";

        if (ex instanceof BaseAccidentException) {
            if (((BaseAccidentException) ex).getLogFlag()) {
                logger.error(operatorLogStr + ex.getMessage() + logTemplate, request.getServletPath(),
                        parameterValues, ex);
            } else {
                logger.warn(operatorLogStr + ex.getMessage() + logTemplate, request.getServletPath(),
                        parameterValues, ex);
            }
            return HttpResultHandler.getErrorResult(((BaseAccidentException) ex).getErrorCode(), ex.getMessage());
        }

        logger.error(operatorLogStr + "uncaught  exception," + logTemplate, request.getServletPath(),
                parameterValues, ex);
        return HttpResultHandler.getErrorResult();
    }

}
