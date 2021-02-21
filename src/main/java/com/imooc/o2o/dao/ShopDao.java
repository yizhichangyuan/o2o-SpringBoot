package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDao {

    /**
     * 根据条件分页查询（可查询条件（可联合查询）：店铺名称（模糊）、店铺Id、店铺类别、店铺状态、区域Id、用户id）
     *
     * @param shopCondition 查询条件
     * @param rowIndex      开始索引号
     * @param pageSize      查询个数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询同等查询int条件下，所有符合条件的个数
     *
     * @param ShopCondition 查询条件
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop ShopCondition);

    /**
     * 插入店铺
     *
     * @param shop
     * @return 1 成功 0 失败
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     *
     * @param shop
     * @return
     */
    int updateShop(Shop shop);

    /**
     * 通过shop id 查询店铺
     *
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);
}
