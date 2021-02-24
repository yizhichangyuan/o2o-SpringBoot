package com.imooc.o2o.web.frontend;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frontend")
public class MainPageController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listmainpageinfo")
    @ResponseBody
    private Map<String, Object> listMainPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            HeadLine headLineCondition = new HeadLine();
            headLineCondition.setEnableStatus(1);
            List<HeadLine> headLineList = headLineService.queryHeadLineList(headLineCondition);
            List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(null);
            PersonInfo user= (PersonInfo)request.getSession().getAttribute("user");
            // 控制前台展示为登陆还是退出登录
            if(user == null){
                modelMap.put("loginStatus", false);
            }else{
                modelMap.put("loginStatus", true);
            }
            modelMap.put("success", true);
            modelMap.put("headLineList", headLineList);
            modelMap.put("shopCategoryList", shopCategoryList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     * 根据店铺查询条件查询符合该条件的店铺列表（查询条件可自由组合，包括一级店铺类别、模糊店铺名、二级店铺类别、区域类别）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshops", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        String shopName = HttpServletRequestUtil.getString(request, "shopName");
        long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
        int areaId = HttpServletRequestUtil.getInt(request, "areaId");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (pageIndex > -1L && pageSize > -1L) {
            Shop shopCondition = compact4ShopCondition(parentId, shopCategoryId, areaId, shopName);
            try {
                ShopExecution shopExecution = shopService.getShopList(shopCondition, pageIndex, pageSize);
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("shopList", shopExecution.getShopList());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
            try {
                int count = shopService.getShopCount(shopCondition);
                modelMap.put("count", count);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    /**
     * 查询shop的条件组合，因为是if所以可以一起组合
     *
     * @param parentId       店铺类别一级id，指明该id可以查询属于所以店铺类别是该一级大类别的子类的所有店铺信息
     * @param shopCategoryId 店铺类别二级id，查询店铺类别时是该店铺类别的所有店铺信息
     * @param areaId         区域id，查询属于该区域的店铺信息
     * @param shopName       店铺名，支持模糊查询
     * @return
     */
    private Shop compact4ShopCondition(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        shopCondition.setEnableStatus(1); // 设置已经通过审核的店铺列表展示
        ShopCategory childCategory = new ShopCategory();
        if (parentId != -1L) {
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
        }
        if (shopCategoryId != -1L) {
            childCategory.setShopCategoryId(shopCategoryId);
        }
        shopCondition.setShopCategory(childCategory);
        if (areaId != -1) {
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if (shopName != null && !shopName.equals("")) {
            shopCondition.setShopName(shopName);
        }
        return shopCondition;
    }

    /**
     * 返回供用户筛选店铺的选择器内容，包括店铺类别选择和区域选择
     * 其中店铺类别如果选择是在全部商店进入，是选择全部的一级类别；如果是从一级类别进入，则选择该类别下的所有子类类别；判断标准是否指明一级parentId
     *
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getSelectorInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        if (parentId != -1L) {
            try {
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parentCategory = new ShopCategory();
                parentCategory.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parentCategory);
                List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(shopCategoryCondition);
                List<Area> areaList = areaService.queryArea();
                modelMap.put("shopCategoryList", shopCategoryList);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        } else {
            try {
                List<ShopCategory> shopCategoryList = shopCategoryService.queryShopCategory(null);
                List<Area> areaList = areaService.queryArea();
                modelMap.put("shopCategoryList", shopCategoryList);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }
}
