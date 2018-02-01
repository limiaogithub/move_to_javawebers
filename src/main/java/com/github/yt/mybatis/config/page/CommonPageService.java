package com.github.yt.mybatis.config.page;


import com.github.yt.mybatis.handler.QueryHandler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;


public class CommonPageService implements PageConvert {

    @Override
    public void convert(QueryHandler queryHandler, HttpServletRequest request) {
        String pageSize = request.getParameter("pageSize");
        String currentPage = request.getParameter("currentPage");
        Integer pageSize1 = StringUtils.isEmpty(pageSize) ? 10 : Integer.valueOf(pageSize);
        Integer currentPage1 = StringUtils.isEmpty(currentPage) ? 1 : Integer.valueOf(pageSize);
        queryHandler.setStart((currentPage1 - 1) * pageSize1);
        queryHandler.setLimit(pageSize1);
    }
}
