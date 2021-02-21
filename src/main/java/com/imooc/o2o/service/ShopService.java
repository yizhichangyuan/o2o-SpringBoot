package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ShopOperationException;

import java.io.IOException;

public interface ShopService {
    /**
     * @param shop        数据库插入店铺信息
     * @param imageHolder 给商铺图片添加水印
     * @return 操作结果
     */
    ShopExecution addShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException, IOException;

    /**
     * 通过店铺id获取店铺信息
     *
     * @param shopId
     * @return
     */
    Shop getShopByShopId(long shopId);

    /**
     * 更新店铺信息，包括对图片的处理（如果有图片的话）
     *
     * @param shop
     * @param imageHolder
     * @return
     */
    ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) throws ShopOperationException;

    /**
     * @param shopCondition
     * @param pageIndex
     * @return
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    /**
     * 查询同条件的所有shop总数
     *
     * @param shopCondition
     * @return
     */
    int getShopCount(Shop shopCondition);
}
