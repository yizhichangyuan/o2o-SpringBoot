package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProductSellDailyExecution;
import com.imooc.o2o.entity.ProductSellDaily;

import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.imooc.o2o.service
 * @NAME:ProductSellDaily
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 14:09
 */
public interface ProductSellDailyService {
    void dailyCalculate();

    ProductSellDailyExecution queryProductSellDailyList(ProductSellDaily condition, Date beginTime, Date endTime);
}
