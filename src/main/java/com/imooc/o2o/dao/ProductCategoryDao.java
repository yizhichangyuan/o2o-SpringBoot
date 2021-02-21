package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryDao {
    /**
     * 根据店铺信息查询该店铺的所有商品类别（主要通过店铺ID或者商品类别名（模糊））
     *
     * @param productCategoryCondition
     * @return
     */
    List<ProductCategory> queryProductCategoryList(@Param("productCategoryCondition") ProductCategory productCategoryCondition,
                                                   @Param("rowIndex") int rowIndex,
                                                   @Param("pageSize") int pageSize);

    /**
     * 查询符合条件的类别分类
     *
     * @param productCategoryCondition
     * @return
     */
    int queryProductCategoryCount(@Param("productCategoryCondition") ProductCategory productCategoryCondition);

    int insertProductCategory(@Param("productCategory") ProductCategory productCategory);

    /**
     * 删除某个商品类别
     *
     * @param productCategory
     * @return
     */
    int deleteProductCategory(@Param("productCategoryCondition") ProductCategory productCategory);

    /**
     * 批量插入数据
     *
     * @param productCategoryList
     * @return
     */
    int insertBatchProductCategory(List<ProductCategory> productCategoryList);

}
