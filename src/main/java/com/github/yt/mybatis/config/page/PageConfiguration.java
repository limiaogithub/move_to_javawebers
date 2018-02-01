package com.github.yt.mybatis.config.page;


public class PageConfiguration {

    private static PageConvert pageConvert;

    public static PageConvert create() {
        if (pageConvert != null) {
            return pageConvert;
        }
        pageConvert = new CommonPageService();
        return pageConvert;
    }

}
