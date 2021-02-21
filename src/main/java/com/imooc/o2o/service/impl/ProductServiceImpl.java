package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import com.imooc.o2o.util.RowIndexCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Override
    public ProductExecution queryProductById(long productId) {
        if (productId <= -1) {
            return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
        }
        try {
            Product product = productDao.getProductById(productId);
            if (product == null) {
                return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
            } else {
                return new ProductExecution(ProductStateEnum.SUCCESS, product);
            }
        } catch (Exception e) {
            throw new ProductException("queryProductById errMsg:" + e.getMessage());
        }
    }

    @Override
    public ProductExecution queryProductList(Product product, int pageIndex, int pageSize) {
        int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
        try {
            List<Product> productList = productDao.queryProductList(product, rowIndex, pageSize);
            int count = productDao.queryProductCount(product);
            ProductExecution pe = new ProductExecution(ProductStateEnum.SUCCESS, productList);
            pe.setCount(count);
            return pe;
        } catch (Exception e) {
            throw new ProductException("queryProductList error:" + e.getMessage());
        }
    }

    @Override
    public int queryProductCount(Product product) {
        return productDao.queryProductCount(product);
    }

    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, ImageHolder thumbnail, List<ImageHolder> detailImgList) {
        if ((product == null && thumbnail == null) && (detailImgList == null || detailImgList.size() == 0)) {
            return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
        }
        try {
            // 1.存储缩略图信息，并删除旧的缩略图，如果不为空
            if (thumbnail != null && thumbnail.getImage() != null && thumbnail.getImageName() != null) {
                try {
                    addImg(product, thumbnail);
                    String targetAddr = productDao.getProductById(product.getProductId()).getImgAddr();
                    ImageUtil.deleteFileOrPath(targetAddr);
                } catch (Exception e) {
                    throw new ProductException("删除旧缩略图失败");
                }
            }
            // 2.存储详情图信息，并批量删除旧的详情图，并删除tb_product_img，同时新增
            if (detailImgList != null && detailImgList.size() != 0) {
                try {
                    List<String> targetAddrList = addDetailImgList(product, detailImgList);
                    ProductImg productImgCondition = new ProductImg();
                    productImgCondition.setProductId(product.getProductId());
                    List<ProductImg> deleteImgList = productImgDao.queryProductImgList(productImgCondition);
                    for (int i = 0; i < deleteImgList.size(); i++) {
                        ImageUtil.deleteFileOrPath(deleteImgList.get(i).getImgAddr());
                    }
                    productImgDao.deleteBatchProductImg(deleteImgList);
                    List<ProductImg> productImgList = new ArrayList<>();
                    for (int i = 0; i < targetAddrList.size(); i++) {
                        ProductImg productImg = new ProductImg();
                        productImg.setImgAddr(targetAddrList.get(i));
                        productImg.setImgDesc(detailImgList.get(i).getImageDesc());
                        productImg.setPriority(detailImgList.get(i).getPriority());
                        productImg.setProductId(product.getProductId());
                        productImg.setCreateTime(new Date());
                        productImgList.add(productImg);
                    }
                    int effectNum = productImgDao.insertBatchProductImg(productImgList);
                    if (effectNum < productImgList.size()) {
                        return new ProductExecution(ProductStateEnum.INNER_ERROR);
                    }
                } catch (Exception e) {
                    throw new ProductException("批量删除旧缩略图失败");
                }
            }

            // 3.更新product
            product.setLastEditTime(new Date());
            int effectNum = productDao.updateProduct(product);
            if (effectNum <= 0) {
                return new ProductExecution(ProductStateEnum.INNER_ERROR);
            }
        } catch (Exception e) {
            throw new ProductException("modifyProduct errMsg: " + e.getMessage());
        }
        return new ProductExecution(ProductStateEnum.SUCCESS);
    }

    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> detailImgList) {
        if (product == null || product.getShop() == null || product.getShop().getShopId() == null) {
            return new ProductExecution(ProductStateEnum.NULL_PRODUCT);
        }
        try {
            product.setEnableStatus(1);
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            if (product.getPriority() == null) {
                product.setPriority(0);
            }
            int effectNum = productDao.insertProduct(product);
            if (effectNum <= 0) {
                throw new ProductException("添加product失败");
            } else {
                if (thumbnail != null) {
                    try {
                        addImg(product, thumbnail);
                    } catch (Exception e) {
                        throw new ProductException("addImg error：" + e.getMessage());
                    }
                    effectNum = productDao.updateProduct(product);
                    if (effectNum <= 0) {
                        return new ProductExecution(ProductStateEnum.INNER_ERROR);
                    }
                }

                List<String> targetAddrList = null;
                if (detailImgList != null && detailImgList.size() > 0) {
                    try {
                        targetAddrList = addDetailImgList(product, detailImgList);
                    } catch (Exception e) {
                        throw new ProductException("addProductImgList error：" + e.getMessage());
                    }
                    List<ProductImg> productImgList = new ArrayList<>();
                    for (String tempAddr : targetAddrList) {
                        ProductImg productImg = new ProductImg();
                        productImg.setProductId(product.getProductId());
                        productImg.setCreateTime(new Date());
                        productImg.setPriority(0);
                        productImg.setImgAddr(tempAddr);
                        productImg.setImgDesc(null);
                        productImgList.add(productImg);
                    }
                    effectNum = productImgDao.insertBatchProductImg(productImgList);
                    if (effectNum != productImgList.size()) {
                        return new ProductExecution(ProductStateEnum.INNER_ERROR);
                    }
                }
            }
        } catch (Exception e) {
            throw new ProductException("addProduct errMsg：" + e.getMessage());
        }
        return new ProductExecution(ProductStateEnum.SUCCESS);
    }

    public List<String> addDetailImgList(Product product, List<ImageHolder> detailImageHolder) throws IOException {
        List<String> targetAddrList = new ArrayList<>();
        for (int i = 0; i < detailImageHolder.size(); i++) {
            String targetAddr = PathUtil.getProductDetailImgPath(product.getShop().getShopId(), product.getProductId());
            String productImgAddr = ImageUtil.saveDetailImg(detailImageHolder.get(i), targetAddr);
            targetAddrList.add(productImgAddr);
        }
        return targetAddrList;
    }

    private void addImg(Product product, ImageHolder thumbnail) throws IOException {
        String targetAddr = PathUtil.getProductSimpleImgPath(product.getShop().getShopId(), product.getProductId());
        String thumbnailImgAddr = ImageUtil.generateThumbnail(thumbnail, targetAddr);
        product.setImgAddr(thumbnailImgAddr);
    }
}
