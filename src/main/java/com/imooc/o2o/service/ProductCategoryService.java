package com.imooc.o2o.service;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    /**
     * 查询指定店铺的商品类别列表
     *
     * @param productCategoryCondition 查询条件：可以是商品类别名或者店铺id
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductCategoryExecution getProductCategoryList(ProductCategory productCategoryCondition, int pageIndex, int pageSize);

    /**
     * 删除商品类别，所有属于该商品类别的商品外键都指向到未分类
     *
     * @param productCategory
     * @return
     */
    ProductCategoryExecution deleteProductCategory(ProductCategory productCategory);

    /**
     * 添加商品类别
     *
     * @param productCategory
     * @return
     */
    ProductCategoryExecution addProductCategory(ProductCategory productCategory);


    ProductCategoryExecution addBatchProductCategory(List<ProductCategory> productCategoryList);
}
