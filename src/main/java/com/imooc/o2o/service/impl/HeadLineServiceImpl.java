package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.HeadLineDao;
import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.exceptions.HeadLineException;
import com.imooc.o2o.service.HeadLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
//    @Autowired
//    private JedisUtil.Keys jedisKeys;
//    @Autowired
//    private JedisUtil.Strings jedisStrings;
//
//    private static Logger logger = LoggerFactory.getLogger(HeadLineService.class);
//
//    @Override
//    public List<HeadLine> queryHeadLineList(HeadLine headLineCondition){
//        ObjectMapper mapper = new ObjectMapper();
//        List<HeadLine> headLineList = null;
//        String key = HEADLINE_KEY; //不带status的条件
//        // 由于headLineCondition可能根据status进行查询，所以这里的key要根据不同的status进行划分
//        // 三种key: headlistlist, headlistlist_0, headlinelist_1
//        if(headLineCondition != null && headLineCondition.getEnableStatus() != null){
//            key = HEADLINE_KEY + "_" + headLineCondition.getEnableStatus();
//        }
//        if(!jedisKeys.exists(key)){
//            headLineList = headLineDao.queryHeadLineList(headLineCondition);
//            try{
//                String jsonString = mapper.writeValueAsString(headLineList);
//                jedisStrings.set(key, jsonString);
//            } catch (JsonProcessingException e) {
//                logger.error("queryHeadLineList error:" + e.getMessage());
//                e.printStackTrace();
//                throw new HeadLineException("queryHeadLineList error:" + e.getMessage());
//            }
//        }else{
//            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
//            String jsonString = jedisStrings.get(key);
//            try {
//                headLineList = mapper.readValue(jsonString, javaType);
//            } catch (IOException e) {
//                logger.error("queryHeadLineList error:" + e.getMessage());
//                e.printStackTrace();
//                throw new HeadLineException("queryHeadLineList error:" + e.getMessage());
//            }
//        }
//        return headLineList;
//    }

    @Override
    public List<HeadLine> queryHeadLineList(HeadLine headLineCondition) {
        return headLineDao.queryHeadLineList(headLineCondition);
    }
}
