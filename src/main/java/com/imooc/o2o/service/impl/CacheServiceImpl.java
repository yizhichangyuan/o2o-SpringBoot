package com.imooc.o2o.service.impl;//package com.imooc.o2o.service.impl;

import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CacheServiceImpl implements CacheService {
    @Override
    public void removeKeys(String prefix) {

    }
//    @Autowired
//    private JedisUtil.Keys keys;
//
//    @Override
//    public void removeKeys(String prefix) {
//        // 首先找到所有以prefix为前缀的key集合
//        Set<String> set = keys.keys(prefix + "*");
//        for(String key : set){
//            keys.del(key);
//        }
//    }
}
