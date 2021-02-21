package com.imooc.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.cache.JedisUtil;
import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
//    @Autowired
//    private JedisUtil.Keys keys;
//    @Autowired
//    private JedisUtil.Strings strings;
//
//    private Logger logger = LoggerFactory.getLogger(ShopCategoryServiceImpl.class);
//
//    @Override
//    public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition) {
//        String key = SHOP_CATEGORY_KEY;
//        List<ShopCategory> list = null;
//        ObjectMapper mapper = new ObjectMapper();
//        if(shopCategoryCondition == null){
//            key = SHOP_CATEGORY_KEY + "_parent_null";
//        }else if(shopCategoryCondition.getParent() != null && shopCategoryCondition.getParent().getShopCategoryId() != null){
//            key = SHOP_CATEGORY_KEY + "_parent_" + shopCategoryCondition.getParent().getShopCategoryId();
//        }else{
//            key = SHOP_CATEGORY_KEY + "_parent_not_null_all";
//        }
//
//        if(!keys.exists(key)){
//            list = shopCategoryDao.queryShopCategory(shopCategoryCondition);
//            try {
//                String jsonString = mapper.writeValueAsString(list);
//                strings.set(key, jsonString);
//            } catch (JsonProcessingException e) {
//                logger.error("queryShopCategory error:" + e.getMessage());
//                e.printStackTrace();
//                throw new ShopOperationException("queryShopCategory error:" + e.getMessage());
//            }
//        }else{
//             String jsonString = strings.get(key);
//             JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
//            try {
//                list = mapper.readValue(jsonString, javaType);
//            } catch (IOException e) {
//                logger.error("queryShopCategory error:" + e.getMessage());
//                e.printStackTrace();
//                throw new ShopOperationException("queryShopCategory error:" + e.getMessage());
//            }
//        }
//        return list;
//    }
@Override
public List<ShopCategory> queryShopCategory(ShopCategory shopCategoryCondition) {
    return shopCategoryDao.queryShopCategory(shopCategoryCondition);
}
}
