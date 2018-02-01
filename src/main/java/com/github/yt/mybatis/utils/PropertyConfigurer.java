package com.github.yt.mybatis.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertyConfigurer extends PropertyPlaceholderConfigurer {
    private static Map<String, String> propertiesMap = new HashMap<>();

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);

        for (Object key : props.keySet()) {
            propertiesMap.put(key.toString(), props.get(key).toString());
        }
    }

    public static String getProperty(String key) {
        return propertiesMap.get(key);
    }
}
