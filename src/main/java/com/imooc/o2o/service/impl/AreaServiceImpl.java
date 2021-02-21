package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.exceptions.AreaException;
import com.imooc.o2o.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
//    @Autowired
//    private JedisUtil.Keys jedisKeys;
//    @Autowired
//    private JedisUtil.Strings jedisStrings;
//
//    private static Logger logger = LoggerFactory.getLogger(AreaServiceImpl.class);
//
//    @Override
//    public List<Area> queryArea(){
//        String key = AREALISTKEY;
//        List<Area> areaList = null;
//        // Object转为Json字符串需要用到
//        ObjectMapper mapper = new ObjectMapper();
//        // 如果redis不存在，则从数据库取出并以Strings类型放入到redis中
//        if(!jedisKeys.exists(key)){
//            areaList = areaDao.queryArea();
//            try {
//                String jsonString = mapper.writeValueAsString(areaList);
//                jedisStrings.set(key, jsonString);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage());
//                throw new AreaException("queryArea error:" + e.getMessage()); //抛出可以回滚事务
//            }
//        }else{ //如果缓存中存在，则从缓存中取出
//            String jsonString = jedisStrings.get(key);
//            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Area.class);
//            try {
//                areaList = mapper.readValue(jsonString, javaType);
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//                e.printStackTrace();
//                throw new AreaException("queryArea error:" + e.getMessage()); //抛出可以回滚事务
//            }
//        }
//        return areaList;
//    }

    @Override
    public List<Area> queryArea() {
        return areaDao.queryArea();
    }
}
