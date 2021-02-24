package com.imooc.o2o.web.ShopAdmin;

import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserShopMapStateEnum;
import com.imooc.o2o.service.UserProductMapService;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:UserShopMapController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 11:11
 */
@RestController
@RequestMapping(value="/shopadmin")
public class UserShopMapController {
    @Autowired
    private UserShopMapService userShopMapService;
    private static Logger logger = LoggerFactory.getLogger(UserShopMapController.class);

    @RequestMapping(value="/getusershoplistbyshop")
    public Map<Object, Object> getUserShopListByShop(HttpServletRequest request) {
        Map<Object, Object> modelMap = new HashMap<>();
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (shop != null && shop.getShopId() != null && pageIndex > -1 && pageSize > -1) {
            try {
                UserShopMap userShopMap = new UserShopMap();
                userShopMap.setShop(shop);
                String userName = HttpServletRequestUtil.getString(request, "userName");
                if(userName != null){
                    PersonInfo personInfo = new PersonInfo();
                    personInfo.setName(userName);
                    userShopMap.setUser(personInfo);
                }
                UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapList(userShopMap, pageIndex, pageSize);
                if (userShopMapExecution.getState() == UserShopMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("userShopMapList", userShopMapExecution.getUserShopMapList());
                    modelMap.put("count", userShopMapExecution.getCount());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", userShopMapExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                logger.error(e.getMessage());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    /**
     * 用户登陆前台，获取用户的积分
     */
    @RequestMapping(value="/getusershopbyuser")
    public Map<Object, Object> getUserShopByUser(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        PersonInfo personInfo = (PersonInfo)request.getSession().getAttribute("user");
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(personInfo == null || personInfo.getUserId() == null){
            modelMap.put("success", false);
            modelMap.put("errMsg", "尚未登陆");
        }else{
            if(shopId > -1){
                UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapById(personInfo.getUserId(), shopId);
                if(userShopMapExecution.getState() == UserShopMapStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                    modelMap.put("point", userShopMapExecution.getUserShopMap().getPoint());
                }
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", "empty shopId");
            }
        }
        return modelMap;
    }
}
