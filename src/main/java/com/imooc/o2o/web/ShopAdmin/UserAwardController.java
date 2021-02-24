package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.*;
import com.imooc.o2o.entity.*;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:UserAwardController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 19:58
 */
@RestController
@RequestMapping(value="/shopadmin")
public class UserAwardController {
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private WechatAuthService wechatAuthService;

    private static Logger logger = LoggerFactory.getLogger(UserAwardController.class);
    /**
     * 用户端和商家获取兑换记录列表
     * @param request
     * @return
     */
    @RequestMapping(value="/getuserawardmaplist")
    public Map<Object,Object> getUserAwardMapList(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 判断是来自用户端还是商家
        boolean isFromFront = HttpServletRequestUtil.getBoolean(request, "isFromFront");
        if(pageIndex >= -1 && pageSize >= -1) {
            UserAwardMap userAwardMap = new UserAwardMap();
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if(awardName != null){
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMap.setAward(award);
            }
            try {
                // 来自用户端，是用户查询自己的兑换记录
                if (isFromFront) {
                    PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                    if (user == null) {
                        modelMap.put("success", false);
                        modelMap.put("errMsg", "尚未登录");
                        return modelMap;
                    } else {
                        userAwardMap.setUser(user);
                        return getExecutionResult(pageIndex, pageSize, userAwardMap);
                    }
                } else {
                    // 来自商家，则查询该店铺的兑换记录
                    Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                    userAwardMap.setShop(currentShop);
                    return getExecutionResult(pageIndex, pageSize, userAwardMap);
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageIndex or pageSize");
            return modelMap;
        }
    }

    /**
     * 操作员扫码改变领取状态
     * @param request
     * @return
     */
    @RequestMapping(value="/awardexchange")
    public ModelAndView exchangeAward(HttpServletRequest request) {
        ModelAndView exchangeSuccess = new ModelAndView("shop/exchangesuccess");
        ModelAndView exchangeFail = new ModelAndView("shop/exchangefail");
        String stateStr = HttpServletRequestUtil.getString(request, "state");
        String jsonStr = stateStr.replaceAll("aaa", "\"");
        ObjectMapper objectMapper = new ObjectMapper();
        WechatInfo wechatInfo = null;
        try {
            wechatInfo = objectMapper.readValue(jsonStr, WechatInfo.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 检查二维码是否过期
        if(!checkQRVaild(wechatInfo)){
            return exchangeFail;
        }

        // 获取回传得到的用户信息
        String code = request.getParameter("code");
        PersonInfo personInfo = null;
        try {
            // 获取操作员信息
            UserAccessToken userAccessToken = WechatUtil.getUserAccessToken(code);
            String openId = userAccessToken.getOpenId();
            WechatAuth wechatAuth = wechatAuthService.getWechatAuthById(openId);
            PersonInfo operator = wechatAuth.getPersonInfo();
            // 获取奖品信息
            UserAwardMap userAwardMap = userAwardMapService.queryUserAwardMapById(wechatInfo.getUserAwardId());
            // 如果确实是该店操作员，则修改该奖品领取状态，更新时间和操作员
            if (checkOpenIdHavePrivilege(operator, userAwardMap)){
                userAwardMap.setLastEditTime(new Date());
                userAwardMap.setOperator(operator);
                userAwardMap.setUsedStatus(1);
                UserAwardMapExecution userAwardMapExecution = userAwardMapService.modifyUserAwardMap(userAwardMap);
                if(userAwardMapExecution.getState() == UserAwardMapStateEnum.SUCCESS.getState()){
                    logger.info(userAwardMap.getAward().getAwardName() + "兑换成功");
                    return exchangeSuccess;
                }else{
                    return exchangeFail;
                }
            }else{
                return exchangeFail;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return exchangeFail;
        }
    }

    private boolean checkQRVaild(WechatInfo wechatInfo){
        Long createTime = wechatInfo.getCreateTime();
        Long currentTime = System.currentTimeMillis();
        if(currentTime - createTime > 600000){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 检查该openId是否为该店铺操作员
     * @return
     */
    private boolean checkOpenIdHavePrivilege(PersonInfo operator, UserAwardMap userAwardMap){
        // 操作员信息为空或奖品信息为空
        if(operator == null || userAwardMap == null){
            return false;
        }
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 0, 1000);
        List<ShopAuthMap> list = shopAuthMapExecution.getShopAuthMapList(); // 是根据user_auth_id升序排列
        for(ShopAuthMap temp : list){
            if(temp.getEmployee().getUserId() == operator.getUserId()){
                return true;
            }
        }
        return false;
    }

    private Map<Object, Object> getExecutionResult(int pageIndex, int pageSize, UserAwardMap userAwardMap) {
        Map<Object, Object> modelMap = new HashMap<>();
        UserAwardMapExecution userAwardMapExecution =
                userAwardMapService.queryUserAwardMapList(userAwardMap, pageIndex, pageSize);
        if(userAwardMapExecution.getState() == UserAwardMapStateEnum.SUCCESS.getState()){
            modelMap.put("success", true);
            modelMap.put("userAwardMapList", userAwardMapExecution.getList());
            modelMap.put("count", userAwardMapExecution.getCount());
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", userAwardMapExecution.getStateInfo());
        }
        return modelMap;
    }

}
