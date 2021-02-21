package com.imooc.o2o.dao;

import com.imooc.o2o.entity.UserProductMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductMapDao {
    List<UserProductMap> queryUserProductList(@Param("userProduct") UserProductMap userProductMap, @Param("rowIndex") int rowIndex,
                                              @Param("pageSize") int pageSize);

    int queryUserProductCount(@Param("userProduct")UserProductMap userProductMap);

    int insertUserProduct(UserProductMap userProductMap);
}
