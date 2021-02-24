package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductSellDaily;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductSellDailyDao {
    List<ProductSellDaily> queryProductSellDailyList(@Param("productSellDaily") ProductSellDaily productSellDaily,
                                                 @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    int insertProductSellDaily();

    int insertDefaultProductSellDaily();
}
