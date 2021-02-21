package com.imooc.o2o.service;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;

import java.util.List;

public interface ProductService {
    ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> detailImgList);

    ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> detailImgList);

    ProductExecution queryProductById(long productId);

    ProductExecution queryProductList(Product product, int pageIndex, int pageSize);

    int queryProductCount(Product product);
}
