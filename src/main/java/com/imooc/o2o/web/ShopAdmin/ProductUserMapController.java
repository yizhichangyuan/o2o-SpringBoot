package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.*;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:ProductUserMapController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 16:09
 */
@RestController
@RequestMapping(value="/shopadmin")
public class ProductUserMapController {
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    private static Logger logger = LoggerFactory.getLogger(ProductUserMapController.class);

    @RequestMapping(value="/listuserproductmapbyshop")
    public Map<Object, Object> getUserProductMapList(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        Shop shop = (Shop)request.getSession().getAttribute("currentShop");
        UserProductMap userProductMap = new UserProductMap();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if(shop != null && shop.getShopId() != null && pageIndex > -1 && pageSize > -1){
            userProductMap.setShop(shop);
            // 如果前端还需要查询某件商品的销售情况进行模糊查询，则传入进去
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if(productName != null){
                Product product = new Product();
                product.setProductName(productName);
                userProductMap.setProduct(product);
            }
            UserProductMapExecution userProductExecution = userProductMapService.queryUserProductMapList(userProductMap, pageIndex, pageSize);
            if(userProductExecution.getState() == UserProductMapStateEnum.SUCCESS.getState()){
                modelMap.put("success", true);
                modelMap.put("userProductMapList", userProductExecution.getList());
                modelMap.put("count", userProductExecution.getCount());
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", userProductExecution.getStateInfo());
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
        }
        return modelMap;
    }

    @RequestMapping(value="productexchange")
    public ModelAndView productExchange(HttpServletRequest request){
        ModelAndView exchangeSuccess = new ModelAndView("shop/exchangesuccess");
        ModelAndView exchangeFail = new ModelAndView("shop/exchangefail");

        // 获取传递信息
        String code = HttpServletRequestUtil.getString(request, "code");
        String stateStr = HttpServletRequestUtil.getString(request, "state");
        String jsonStr = stateStr.replaceAll("aaa", "\"");
        ObjectMapper objectMapper = new ObjectMapper();
        WechatInfo wechatInfo = null;
        try{
             wechatInfo = objectMapper.readValue(jsonStr, WechatInfo.class);
        }catch (Exception e){
            return exchangeFail;
        }

        // 获取操作员信息
        UserAccessToken userAccessToken = WechatUtil.getUserAccessToken(code);
        String openId = userAccessToken.getOpenId();
        WechatAuth wechatAuth = wechatAuthService.getWechatAuthById(openId);
        PersonInfo operator = wechatAuth.getPersonInfo();

        // 查询该商品和店铺信息
        ProductExecution pe = productService.queryProductById(wechatInfo.getProductId());
        if(pe.getState() != ProductStateEnum.SUCCESS.getState()){
            return exchangeFail;
        }
        Shop shop = pe.getProduct().getShop();
        Product product = pe.getProduct();

        // 检查扫码者是否为店员
        if(!checkOperatorStatus(operator, shop)){
            return exchangeFail;
        }

        try{
            UserProductMap userProductMap = new UserProductMap();
            PersonInfo user = new PersonInfo();
            user.setUserId(wechatInfo.getCustomerId());
            userProductMap.setUser(user);
            userProductMap.setShop(shop);
            userProductMap.setProduct(product);
            userProductMap.setOperator(operator);
            userProductMap.setPoint(product.getPoint());
            UserProductMapExecution upe = userProductMapService.addUserProductMap(userProductMap);
            if(upe.getState() == UserProductMapStateEnum.SUCCESS.getState()){
                return exchangeSuccess;
            }else{
                logger.error(upe.getStateInfo());
                return exchangeFail;
            }
        }catch(Exception e){
            return exchangeFail;
        }
    }

    private boolean checkOperatorStatus(PersonInfo personInfo, Shop shop) {
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setShop(shop);
        ShopAuthMapExecution sae = shopAuthMapService.listShopAuthMapByShopId(shop.getShopId(), 0, 1000);
        List<ShopAuthMap> list = sae.getShopAuthMapList();
        for(ShopAuthMap temp : list){
            if(temp.getEmployee().getUserId() == personInfo.getUserId()){
                return true;
            }
        }
        return false;
    }
}
