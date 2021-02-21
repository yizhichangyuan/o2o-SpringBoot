package com.imooc.o2o.dao;

import com.imooc.o2o.entity.UserShopMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserShopMapDao {
    int insertUserShopMap(UserShopMap userShopMap);

    int updateUserShopMap(@Param("shopId") long shopId, @Param("userId") long userId, @Param("point") int point);

    UserShopMap queryUserShopMapById(@Param("userId") long userId, @Param("shopId") long shopId);

    List<UserShopMap> queryUserShopMapList(@Param("userShop") UserShopMap userShopMap, @Param("rowIndex") int rowIndex,
                                       @Param("pageSize") int pageSize);

    int queryUserShopMapCount(@Param("userShop") UserShopMap userShopMap);
}
