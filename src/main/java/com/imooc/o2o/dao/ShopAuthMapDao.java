package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ShopAuthMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopAuthMapDao {
    /**
     * 分页查询店铺的授权人员信息
     * @param shopId
     * @param rowIndex
     * @param pageSize
     * @return
     */
    List<ShopAuthMap> queryShopAuthMapListById(@Param("shopId") long shopId, @Param("rowIndex") int rowIndex,
                                               @Param("pageSize") int pageSize);

    /**
     * 查询店铺所有授权员工总数
     * @param shopId
     * @return
     */
    int queryShopAuthMapCount(long shopId);

    /**
     * 对某个员工解除授权
     * @param shopId
     * @param employeeId
     * @return
     */
    int deleteShopAuthMap(@Param("shopId") long shopId, @Param("employeeId") int employeeId);

    /**
     * 更新授权信息
     * @param shopAuthMap
     * @return
     */
    int updateShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 新增授权员工
     * @param shopAuthMap
     * @return
     */
    int insertShopAuthMap(ShopAuthMap shopAuthMap);

    /**
     * 通过ShopAuthId查询某个员工的信息
     * @param shopAuthId
     * @return
     */
    ShopAuthMap queryShopAuthMapById(long shopAuthId);
}
