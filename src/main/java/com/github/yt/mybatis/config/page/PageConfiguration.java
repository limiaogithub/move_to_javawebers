package com.github.yt.mybatis.config.page;


import com.github.yt.mybatis.utils.SpringContextUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class PageConfiguration {

    private static PageConvert pageConvert;

    public static PageConvert create() {
        if (pageConvert != null) {
            return pageConvert;
        }
        Object object = null;
        boolean error = false;
        try {
            object = SpringContextUtils.getBean("ytPageConfig");
        } catch (NoSuchBeanDefinitionException e) {
            error = true;
        }
        if (error || object == null) {
            pageConvert = new CommonPageService();
        } else {
            pageConvert = (PageConvert) object;
        }
        return pageConvert;
    }

}
