package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ProductCategoryExecution;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductCategoryStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ProductCategoryService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop == null) {
            currentShop = new Shop();
            currentShop.setShopId(8L);
            request.getSession().setAttribute("currentShop", currentShop);
        }
        long shopId = currentShop.getShopId();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setShopId(shopId);
        if (shopId <= -1) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "传入的店铺Id非法");
        } else {
            try {
                ProductCategoryExecution pce = productCategoryService.getProductCategoryList(productCategory, 1, 100);
                List<ProductCategory> productCategoryList = pce.getProductCategoryList();
                if (productCategoryList == null || productCategoryList.size() == 0) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "您还未创建过商品类别");
                } else {
                    modelMap.put("success", true);
                    modelMap.put("productCategoryList", productCategoryList);
                    modelMap.put("productCategoryTotalCount", pce.getCount());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    @RequestMapping(value = "/registerproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerProductCategory(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        System.out.println(request.getParameter("priority"));
        System.out.println("验证码为: " + request.getParameter("verifyCodeActual"));
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        ProductCategory productCategory = null;
        // 1.接收表单数据
        ObjectMapper objectMapper = new ObjectMapper();
        String productCategoryStr = HttpServletRequestUtil.getString(request, "productCategory");
        try {
            productCategory = objectMapper.readValue(productCategoryStr, ProductCategory.class);
            Long shopId = (Long) request.getSession().getAttribute("currentShop");
            if (shopId == null) {
                shopId = 8L;
            }
            productCategory.setShopId(shopId);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        try {
            ProductCategoryExecution pce = productCategoryService.addProductCategory(productCategory);
            if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", pce.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/addbatchproductcategory", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addBatchProductCategory(@RequestBody List<ProductCategory> productCategoryList, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        Long shopId = currentShop.getShopId();
        for (ProductCategory productCategory : productCategoryList) {
            productCategory.setShopId(shopId);
        }
        try {
            ProductCategoryExecution execution = productCategoryService.addBatchProductCategory(productCategoryList);
            if (execution.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", execution.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/deleteproductcategory", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> deleteProductCategory(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 必须要店铺id，而且要从session中获取，否则其他人会删除某个店铺的商品类别
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop == null) {
            currentShop = new Shop();
            currentShop.setShopId(8L);
        }
        Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
        ProductCategory productCategory = new ProductCategory();
        productCategory.setShopId(currentShop.getShopId());
        productCategory.setProductCategoryId(productCategoryId);
        try {
            ProductCategoryExecution pce = productCategoryService.deleteProductCategory(productCategory);
            if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", pce.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}
