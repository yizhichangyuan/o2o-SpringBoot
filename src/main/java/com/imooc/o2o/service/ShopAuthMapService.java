package com.imooc.o2o.service;

import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.entity.ShopAuthMap;

/**
 * @PackageName:com.imooc.o2o.service
 * @NAME:ShopAuthMapService
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/19 21:17
 */
public interface ShopAuthMapService {
    /**
     * 根据传入店铺id分页查询结果
     * @param shopId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize);

    /**
     * 根据shopAuthId返回相应的授权信息
     * @param shopAuthId
     * @return
     */
    ShopAuthMap getShopAuthMapById(Long shopAuthId);

    /**
     * 添加授权信息
     * @param shopAuthMap
     * @return
     */
    ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 更新授权信息，包括职位、状态等
     * @param shopAuthMap
     * @return
     */
    ShopAuthMapExecution modifyShopAUthMap(ShopAuthMap shopAuthMap);
}
