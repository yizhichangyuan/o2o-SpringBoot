package com.imooc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.AwardExecution;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.AwardStateEnum;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.enums.UserShopMapStateEnum;
import com.imooc.o2o.service.AwardService;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.web.ShopAdmin.ShopAuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @PackageName:com.imooc.o2o.web.frontend
 * @NAME:AwardController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/23 11:14
 */
@RestController
@RequestMapping(value = "/frontend")
@Configuration
public class AwardExchangeController {
    // 微信获取用户信息的开发者appid
    private static String appId;
    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加授权信息的url
    private static String awardExchangeUrl;
    private static Logger logger = LoggerFactory.getLogger(ShopAuthController.class);
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private UserShopMapService userShopMapService;

    @Value("${wechat.appid}")
    public void setAppId(String appId) {
        AwardExchangeController.appId = appId;
    }

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        AwardExchangeController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        AwardExchangeController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        AwardExchangeController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.awardexchange.url}")
    public void setAwardExchangeUrl(String awardExchangeUrl) {
        AwardExchangeController.awardExchangeUrl = awardExchangeUrl;
    }

    @RequestMapping(value = "/getawardlistbyshop", method = RequestMethod.GET)
    public Map<Object, Object> getAwardListByShop(HttpServletRequest request) {
        Map<Object, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        Award award = new Award();
        String awardName = HttpServletRequestUtil.getString(request, "awardName");
        if (awardName != null) {
            award.setAwardName(awardName);
        }
        if (pageIndex > -1 && pageSize > -1) {
            try {
                long shopId = HttpServletRequestUtil.getLong(request, "shopId");
                if (shopId > -1) {
                    award.setShopId(shopId);
                    award.setEnableStatus(1);
                }
                AwardExecution awardExecution = awardService.queryAwardList(award, pageIndex, pageSize);
                if (awardExecution.getState() == AwardStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("awardList", awardExecution.getAwardList());
                    modelMap.put("count", awardExecution.getCount());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", awardExecution.getStateInfo());
                }

                PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
                if (user == null) {
                    modelMap.put("totalPoint", 0);
                } else {
                    UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapById(user.getUserId(), shopId);
                    if (userShopMapExecution.getState() == UserShopMapStateEnum.SUCCESS.getState() && userShopMapExecution.getUserShopMap() != null) {
                        modelMap.put("totalPoint", userShopMapExecution.getUserShopMap().getPoint());
                    } else {
                        modelMap.put("totalPoint", 0);
                    }
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

    @RequestMapping(value = "/awardexchange", method = RequestMethod.GET)
    public Map<Object, Object> awardExchange(HttpServletRequest request) {
        Map<Object, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user"); // 拦截器拦截必须登陆
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Award award = awardService.queryAwardById(awardId);
        if (award == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "奖品不存在");
        } else {
            UserAwardMap userAwardMap = new UserAwardMap();
            userAwardMap.setAward(award);
            userAwardMap.setUser(user);
            Shop shop = new Shop();
            shop.setShopId(shopId);
            userAwardMap.setShop(shop);
            userAwardMap.setOperator(null);
            userAwardMap.setPoint(award.getPoint());
            try {
                UserAwardMapExecution execution = userAwardMapService.addUserAwardMap(userAwardMap);
                if (execution.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", execution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    @RequestMapping(value = "/getuserawardlistbyuser")
    public Map<Object, Object> getUserAwardListByUser(HttpServletRequest request) {
        Map<Object, Object> modelMap = new HashMap<>();
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        if (user == null || user.getUserId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "未登录");
            return modelMap;
        }
        UserAwardMap userAwardMap = new UserAwardMap();
        userAwardMap.setUser(user);
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if (pageIndex > -1 || pageSize > -1) {
            try {
                UserAwardMapExecution execution = userAwardMapService.queryUserAwardMapList(userAwardMap, pageIndex, pageSize);
                if (execution.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("count", execution.getCount());
                    modelMap.put("userAwardList", execution.getList());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", execution.getStateInfo());
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

    @RequestMapping(value = "/getuserawardbyid")
    public Map<Object, Object> getUserAwardListById(HttpServletRequest request) {
        Map<Object, Object> modelMap = new HashMap<>();
        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        try {
            UserAwardMap userAwardMap = userAwardMapService.queryUserAwardMapById(userAwardId);
            modelMap.put("success", true);
            modelMap.put("userAwardMap", userAwardMap);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "generateawardexchange")
    public void generateAwardExchange(HttpServletRequest request, HttpServletResponse response) {
        Long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        try {
            UserAwardMap userAwardMap = userAwardMapService.queryUserAwardMapById(userAwardId);
            long timeStamp = System.currentTimeMillis();
            String content = String.format("{aaauserAwardIdaaa:%s, aaacreateTimeaaa:%s}", userAwardId, timeStamp);
            // 确实未发生兑换，则展示兑换二维码
            if (userAwardMap.getUsedStatus() == 0) {
                String url = urlPrefix + awardExchangeUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                String shortUrl = ShortNetAddressUtil.getShortURL(url);
                BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(shortUrl, response);
                // 将二维码图片流输出到响应流中
                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
            }
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }
}
