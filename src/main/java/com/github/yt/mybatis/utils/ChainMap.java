package com.github.yt.mybatis.utils;

import java.util.LinkedHashMap;
import java.util.Map;


public class ChainMap<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -8256232046919043447L;

    /**
     * 链表式赋值，返回值为map对象本身
     *
     * @param key   键
     * @param value 值
     * @return 返回map本身
     */
    public ChainMap<K, V> chainPut(K key, V value) {
        super.put(key, value);
        return this;
    }

    public ChainMap<K,V> chainPutAll(Map map){
        if(map!=null) {
            super.putAll(map);
        }
        return this;
    }

}
