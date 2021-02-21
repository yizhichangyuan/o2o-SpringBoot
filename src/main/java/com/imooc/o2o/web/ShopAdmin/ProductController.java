package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.exceptions.ProductException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shopadmin")
public class ProductController {
    @Autowired
    private ProductService productService;

    // 限制用户最大上传详情图数量
    private static final int MAX_UPLOAD = 6;

    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addProduct(HttpServletRequest request) {
        System.out.println("hello");
        Map<String, Object> modelMap = new HashMap<>();
        // 1.首先进行验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }

        // 2.获取product对象
        ObjectMapper objectMapper = new ObjectMapper();
        Product product;
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = objectMapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // 3.获取缩略图和详情图
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        ImageHolder thumbnail = new ImageHolder();
        List<ImageHolder> detailImgList = new ArrayList<>();
        if (resolver.isMultipart(request)) {
            try {
                getImageHolder(request, thumbnail, detailImgList);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        // 4.存储图片和商品信息
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        if (shop == null) {
            shop = new Shop();
            shop.setShopId(8L);
        }
        product.setShop(shop);
        try {
            ProductExecution productExecution = productService.addProduct(product, thumbnail, detailImgList);
            if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", productExecution.getStateInfo());
            }
        } catch (ProductException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    private void getImageHolder(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> detailImgList) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
        thumbnail.setImage(commonsMultipartFile.getInputStream());
        thumbnail.setImageName(commonsMultipartFile.getOriginalFilename());
        for (int i = 0; i < MAX_UPLOAD; i++) {
            commonsMultipartFile = (CommonsMultipartFile) multipartHttpServletRequest.getFile("productDetailImg-" + i);
            if (commonsMultipartFile != null) {
                ImageHolder temp = new ImageHolder(commonsMultipartFile.getInputStream(), commonsMultipartFile.getOriginalFilename());
                detailImgList.add(temp);
            } else {
                break;
            }
        }
    }

    @RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String isPullDown = HttpServletRequestUtil.getString(request, "isPullDown");
        boolean doPullDown = false;
        if (isPullDown != null && isPullDown.equals("true")) {
            doPullDown = true;
        }
        // 1.首先进行验证码校验
        if (!doPullDown && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        // 1.准备获取product对象
        Product product = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            if (productStr != null || !productStr.equals("")) {
                product = objectMapper.readValue(productStr, Product.class);
                Shop shop = (Shop) request.getSession().getAttribute("currentShop");
                if (shop == null) {
                    shop = new Shop();
                    shop.setShopId(8L);
                }
                product.setShop(shop);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "商品信息不能为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2.获取缩略图和详情图列表
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        ImageHolder thumbnail = new ImageHolder();
        List<ImageHolder> detailImgList = new ArrayList<>();
        if (resolver.isMultipart(request)) {
            try {
                getImageHolder(request, thumbnail, detailImgList);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }
        // 3.调用Service层
        try {
            ProductExecution productExecution = productService.modifyProduct(product, thumbnail, detailImgList);
            if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", productExecution.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            long productId = HttpServletRequestUtil.getLong(request, "productId");
            ProductExecution pe = productService.queryProductById(productId);
            if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("product", pe.getProduct());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", pe.getStateInfo());
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getproductlist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getProductList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String productName = HttpServletRequestUtil.getString(request, "productName");
        long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if (currentShop == null) {
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId == -1L) {
                shopId = 8L;
            }
            currentShop = new Shop();
            currentShop.setShopId(shopId);
        }
        Product productCondition = compactCondition(currentShop, productName, productCategoryId);
        try {
            ProductExecution productExecution = productService.queryProductList(productCondition, pageIndex, pageSize);
            if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("productList", productExecution.getProductList());
                modelMap.put("count", productExecution.getCount());
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", productExecution.getStateInfo());
            }
            return modelMap;
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
    }

    private Product compactCondition(Shop shop, String productName, long productCategoryId) {
        Product productCondition = new Product();
        if (shop != null && shop.getShopId() != null) {
            productCondition.setShop(shop);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        if (productCategoryId != -1L) {
            ProductCategory pc = new ProductCategory();
            pc.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(pc);
        }
        return productCondition;
    }

}
