package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductSellDailyDao;
import com.imooc.o2o.dto.ProductSellDailyExecution;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.enums.ProductSellDailyStateEnum;
import com.imooc.o2o.service.ProductSellDailyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.imooc.o2o.service.impl
 * @NAME:ProductSellDailyServiceImpl
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 14:10
 */
@Service
public class ProductSellDailyServiceImpl implements ProductSellDailyService {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;
    private static final Logger logger = LoggerFactory.getLogger(ProductSellDailyServiceImpl.class);

    @Override
    public ProductSellDailyExecution queryProductSellDailyList(ProductSellDaily condition, Date beginTime, Date endTime) {
        if(condition.getShop() == null || condition.getShop().getShopId() == null){
            return new ProductSellDailyExecution(ProductSellDailyStateEnum.NULL_SHOP_Id);
        }
        try{
            List<ProductSellDaily> list = productSellDailyDao.queryProductSellDailyList(condition, beginTime, endTime);
            return new ProductSellDailyExecution(ProductSellDailyStateEnum.SUCCESS, list);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 商品日销量统计
     */
    @Override
    public void dailyCalculate() {
        logger.info("quartz running");
        productSellDailyDao.insertProductSellDaily();
        // 对于当日没有销量的商品插入销量为0的记录
        productSellDailyDao.insertDefaultProductSellDaily();
    }
}
