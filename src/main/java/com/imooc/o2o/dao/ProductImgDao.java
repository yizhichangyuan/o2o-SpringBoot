package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductImg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImgDao {
    int insertBatchProductImg(List<ProductImg> productImgList);

    int deleteBatchProductImg(List<ProductImg> productImgList);

    List<ProductImg> queryProductImgList(@Param("productImg") ProductImg productImg);
}
