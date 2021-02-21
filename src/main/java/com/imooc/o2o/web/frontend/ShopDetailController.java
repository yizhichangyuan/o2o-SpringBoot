package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class ShopDetailController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/shopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopDetailInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        try {
            Shop shop = shopService.getShopByShopId(shopId);
            if (shop != null) {
                modelMap.put("shop", shop);
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        try {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setShopId(shopId);
            ProductCategoryExecution pce = productCategoryService.getProductCategoryList(productCategory, 1, 1000);
            if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("productCategoryList", pce.getProductCategoryList());
                modelMap.put("count", pce.getCount());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", pce.getStateInfo());
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        modelMap.put("success", true);
        return modelMap;
    }

    @RequestMapping(value = "/getproductlist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
        String productName = HttpServletRequestUtil.getString(request, "productName");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Product productCondition = compact3ProductCondition(shopId, productCategoryId, productName);

        if (pageIndex != -1 && pageSize != -1) {
            try {
                ProductExecution pe = productService.queryProductList(productCondition, pageIndex, pageSize);
                int count = productService.queryProductCount(productCondition);
                if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("productList", pe.getProductList());
                    modelMap.put("count", count);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
        }
        return modelMap;
    }

    private Product compact3ProductCondition(long shopId, long productCategoryId, String productName) {
        Product product = new Product();
        product.setEnableStatus(1); // 寻找已经上架的商品
        Shop shop = new Shop();
        shop.setShopId(shopId);
        product.setShop(shop);

        if (productCategoryId != -1L) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }
        if (productName != null && !productName.equals("")) {
            product.setProductName(productName);
        }
        return product;
    }

}
