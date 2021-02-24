package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ProductCategoryException;
import com.imooc.o2o.exceptions.ProductException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.util.RowIndexCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public ProductCategoryExecution getProductCategoryList(ProductCategory productCategoryCondition, int pageIndex, int pageSize) {
        int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
        ProductCategoryExecution productCategoryExecution = new ProductCategoryExecution();
        try {
            List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(productCategoryCondition, rowIndex, pageSize);
            int count = productCategoryDao.queryProductCategoryCount(productCategoryCondition);
            productCategoryExecution.setState(ProductCategoryStateEnum.SUCCESS.getState());
            productCategoryExecution.setStateInfo(ProductCategoryStateEnum.SUCCESS.getStateInfo());
            productCategoryExecution.setProductCategoryList(productCategoryList);
            productCategoryExecution.setCount(count);
            return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategoryList);
        } catch (Exception e) {
            throw new ProductCategoryException("getProductCategoryList error: " + e.getMessage());
        }
    }

    /**
     * 首先应该将相应的商品的对应的商品类别指向为未分类状态，然后再将类别删除
     *
     * @param productCategory
     * @return
     */
    @Override
    public ProductCategoryExecution deleteProductCategory(ProductCategory productCategory) {
        if (productCategory == null || productCategory.getShopId() == null || productCategory.getProductCategoryId() == null) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.NULL_PRODUCT_CATEGORY);
        }
        // todo 首先将对应商品的商品类别设置为未分类
        try {
            int effectNum = productDao.alterProductCategoryToNull(productCategory.getProductCategoryId());
            if (effectNum < 0) {
                throw new ProductException("商品的商品类别置空失败");
            }
            effectNum = productCategoryDao.deleteProductCategory(productCategory);
            if (effectNum >= 0) {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            } else {
                throw new ProductCategoryException("删除商品类别失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryException("deleteProductCategory error :" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution addBatchProductCategory(List<ProductCategory> productCategoryList) {
        if (productCategoryList == null || productCategoryList.size() == 0) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.NULL_PRODUCT_CATEGORY);
        }
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setCreateTime(new Date());
            if (productCategory.getPriority() == null) {
                productCategory.setPriority(0);
            }
        }
        try {
            int successSize = productCategoryDao.insertBatchProductCategory(productCategoryList);
            if (successSize == productCategoryList.size()) {
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            } else {
                throw new ProductCategoryException("批量插入productCategory失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryException("addBatchProductCategory errMsg:" + e.getMessage());
        }
    }

    @Override
    public ProductCategoryExecution addProductCategory(ProductCategory productCategory) {
        if (productCategory == null) {
            return new ProductCategoryExecution(ProductCategoryStateEnum.NULL_PRODUCT_CATEGORY);
        }
        ProductCategoryExecution pce = new ProductCategoryExecution();
        productCategory.setCreateTime(new Date());
        try {
            int success = productCategoryDao.insertProductCategory(productCategory);
            if (success == 1) {
                pce.setState(ProductCategoryStateEnum.SUCCESS.getState());
                pce.setStateInfo(ProductCategoryStateEnum.SUCCESS.getStateInfo());
            } else {
                throw new ProductCategoryException("创建商品类别失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryException("addProductCategory errMsg :" + e.getMessage());
        }
        return pce;
    }
}
