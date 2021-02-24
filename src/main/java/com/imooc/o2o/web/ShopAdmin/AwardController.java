package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:AwardController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 13:41
 */
@RestController
@RequestMapping(value="/shopadmin")
public class AwardController {
    @Autowired
    private AwardService awardService;

    @RequestMapping(value="/getawardlistbyshop", method= RequestMethod.GET)
    public Map<Object,Object> getAwardListByShop(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        boolean isFromFront = HttpServletRequestUtil.getBoolean(request, "isFromFront"); // 判断是来自后台管理还是前台
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Award award = new Award();
        String awardName = HttpServletRequestUtil.getString(request, "awardName");
        if(awardName != null){
            award.setAwardName(awardName);
        }
        if(pageIndex > -1 && pageSize > -1){
            try{
                if(isFromFront == false){
                    Shop shop = (Shop)request.getSession().getAttribute("currentShop");
                    if(shop != null && shop.getShopId() != null){
                        award.setShopId(shop.getShopId());
                    }
                }else {
                    long shopId = HttpServletRequestUtil.getLong(request, "shopId");
                    if (shopId > -1) {
                        award.setShopId(shopId);
                        award.setEnableStatus(1);
                    }
                }
                AwardExecution awardExecution = awardService.queryAwardList(award, pageIndex, pageSize);
                if(awardExecution.getState() == AwardStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                    modelMap.put("awardList", awardExecution.getAwardList());
                    modelMap.put("count", awardExecution.getCount());
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", awardExecution.getStateInfo());
                }
            }catch(Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
        }
        return modelMap;
    }

    @RequestMapping(value="getawardbyid")
    public Map<Object, Object> getAwardById(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        try{
            Award award = awardService.queryAwardById(awardId);
            modelMap.put("success", true);
            modelMap.put("award", award);
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value="modifyaward")
    public Map<Object, Object> modifyAward(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        Award award = null;
        ImageHolder imageHolder = null;
        Shop shop = (Shop)request.getSession().getAttribute("currentShop");
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if(!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        try{
            award = getAwardFromRequest(request);
            award.setShopId(shop.getShopId());
            imageHolder = getAwardImageHolderFromRequest(request);
            AwardExecution awardExecution = awardService.modifyAward(award, imageHolder);
            if(awardExecution.getState() == AwardStateEnum.SUCCESS.getState()){
                modelMap.put("success", true);
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", awardExecution.getStateInfo());
            }
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value="addaward")
    public Map<Object, Object> addAward(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        Shop shop = (Shop)request.getSession().getAttribute("currentShop");
        Award award = null;
        ImageHolder imageHolder = null;
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        try{
            award = getAwardFromRequest(request);
            award.setShopId(shop.getShopId());
            imageHolder = getAwardImageHolderFromRequest(request);
            AwardExecution awardExecution = awardService.addAward(award, imageHolder);
            if(awardExecution.getState() == AwardStateEnum.SUCCESS.getState()){
                modelMap.put("success", true);
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", awardExecution.getStateInfo());
            }
        }catch(Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    private Award getAwardFromRequest(HttpServletRequest request) throws JsonProcessingException {
        String awardStr = HttpServletRequestUtil.getString(request, "awardStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Award award = null;
        award = objectMapper.readValue(awardStr, Award.class);
        return award;
    }

    private ImageHolder getAwardImageHolderFromRequest(HttpServletRequest request) throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        ImageHolder imageHolder = null;
        if(resolver.isMultipart(request)){
            MultipartRequest multipartRequest = (MultipartRequest)request;
            MultipartFile multipartFile = multipartRequest.getFile("awardImg");
            imageHolder = new ImageHolder();
            imageHolder.setImage(multipartFile.getInputStream());
            imageHolder.setImageName(multipartFile.getOriginalFilename());
        }
        return imageHolder;
    }
}
