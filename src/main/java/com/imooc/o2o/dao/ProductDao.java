package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao {
    List<Product> queryProductList(@Param("productCondition") Product productCondition,
                                   @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    int insertProduct(@Param("product") Product product);

    int updateProduct(@Param("product") Product product);

    Product getProductById(long productId);

    int queryProductCount(@Param("productCondition") Product productCondition);

    /**
     * 根据productCategoryId将相应的商品的分类设置为空
     *
     * @param productCategoryId
     * @return
     */
    int alterProductCategoryToNull(long productCategoryId);
}
