package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
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
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    // 从用户的店铺列表点击进入js发送请求的路由
    @RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if (currentShop == null) {
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop shop = new Shop();
            shop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", shop);
            modelMap.put("redirect", false);
            modelMap.put("shopId", shopId);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
        if (owner == null) {
            owner = new PersonInfo();
            owner.setUserId(1L);
            owner.setName("lala");
            request.getSession().setAttribute("user", owner);
        }
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(owner);
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            List<Shop> shopList = shopExecution.getShopList();
            request.getSession().setAttribute("shopList", shopList);
            if (shopList != null) {
                modelMap.put("success", true);
                modelMap.put("shopList", shopList);
                modelMap.put("user", owner);  //便于前端显示用户的名称
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "您还未创建店铺");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getShopByShopId(shopId);
                List<Area> areaList = areaService.queryArea();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 1.验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        // 2.转为Shop对象和图片流
        Shop shop;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile fileImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                fileImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        // 3.调用service层，传入shop对象和图片流
        if (shop != null && shop.getShopId() != null) {
            try {
                ShopExecution shopExecution;
                if (fileImg != null) {
                    ImageHolder thumbnail = new ImageHolder(fileImg.getInputStream(), fileImg.getOriginalFilename());
                    shopExecution = shopService.modifyShop(shop, thumbnail);
                } else {
                    shopExecution = shopService.modifyShop(shop, null);
                }
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传店铺id为空");
            return modelMap;
        }
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        // 1.接收并转化相应的参数，包括店铺信息及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // 图片文件接收
        CommonsMultipartFile shopImg;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);
            try {
                ImageHolder thumbnail = new ImageHolder(shopImg.getInputStream(), shopImg.getOriginalFilename());
                ShopExecution shopExecution = shopService.addShop(shop, thumbnail);
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    // 该用户可以操作的店铺列表，放入到session中，便于店家在创建成功后再管理页面查看自己的店铺
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shop);
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
                return modelMap;
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息或上传店铺图片");
            return modelMap;
        }
    }

//    private static void inputStreamToFile(InputStream inputStream, File file) {
//        OutputStream outputStream = null;
//        int bytesRead;
//        byte[] bytesBuffer = new byte[1024];
//        try {
//            outputStream = new FileOutputStream(file);
//            while (inputStream != null && (bytesRead = inputStream.read(bytesBuffer)) != -1) {
//                outputStream.write(bytesBuffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("调用InputStreamToFile发生异常:" + e.getMessage());
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("inputStreamToFile关闭异常:" + e.getMessage());
//            }
//        }
//    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList;
        List<Area> areaList;

        try {
            shopCategoryList = shopCategoryService.queryShopCategory(new ShopCategory());
            areaList = areaService.queryArea();
            modelMap.put("success", true);
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}
