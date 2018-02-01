package com.github.yt.mybatis.config.page;


import com.github.yt.mybatis.handler.QueryHandler;

import javax.servlet.http.HttpServletRequest;


public interface PageConvert<T> {

    void convert(QueryHandler queryHandler, HttpServletRequest request);

}
