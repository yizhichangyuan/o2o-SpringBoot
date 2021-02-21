package com.imooc.o2o.web.ShopAdmin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.dto.*;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:ShopAuthController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/20 08:54
 */
@RestController
@RequestMapping(value="/shopadmin")
@Configuration
public class ShopAuthController {
    @Autowired
    private ShopAuthMapService shopAuthMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    // 微信获取用户信息的开发者appid
    private static String appId;
    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加授权信息的url
    private static String authUrl;

    private static Logger logger = LoggerFactory.getLogger(ShopAuthController.class);

    @Value("${wechat.appid}")
    public void setAppId(String appId) {
        ShopAuthController.appId = appId;
    }

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ShopAuthController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ShopAuthController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ShopAuthController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setAuthUrl(String authUrl) {
        ShopAuthController.authUrl = authUrl;
    }

    @RequestMapping(value="/listshopauth", method= RequestMethod.GET)
    public Map<Object, Object> listShopAuth(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if(currentShop != null && pageIndex >= -1 && pageSize >= -1){
            try{
                ShopAuthMapExecution execution = shopAuthMapService.listShopAuthMapByShopId(currentShop.getShopId(),
                        pageIndex, pageSize);
                if(execution.getState() == ShopAuthMapStateEnum.AUTH_SUCCESS.getState()){
                    modelMap.put("success", true);
                    modelMap.put("shopAuthList", execution.getShopAuthMapList());
                    modelMap.put("count", execution.getCount());
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", execution.getStateInfo());
                }
            }catch(Exception e){
                logger.error("listShopAuth error:" + e.getMessage());
                e.printStackTrace();
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    @RequestMapping(value="/modifyshopauthbyid", method=RequestMethod.POST)
    public Map<Object, Object> modifyShopAuthById(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        boolean verify = HttpServletRequestUtil.getBoolean(request, "verify");
        if(verify && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码不正确");
            return modelMap;
        }
        String shopAuthStr = HttpServletRequestUtil.getString(request, "shopAuthMapStr");
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            ShopAuthMap shopAuthMap = objectMapper.readValue(shopAuthStr, ShopAuthMap.class);
            // 检查是否为店长，如果为店长那么就不能修改店长的信息
            if(shopAuthMap != null && shopAuthMap.getShopAuthId() != null && checkPermission(shopAuthMap)){
                ShopAuthMapExecution execution = shopAuthMapService.modifyShopAUthMap(shopAuthMap);
                if(execution.getState() == ShopAuthMapStateEnum.AUTH_SUCCESS.getState()){
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", execution.getStateInfo());
                }
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", "无操作权限");
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value="/getshopauthbyid", method = RequestMethod.GET)
    public Map<Object,Object> getShopAuthById(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        long shopAuthId = HttpServletRequestUtil.getLong(request, "shopAuthId");
        if(shopAuthId <= -1){
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopAuthId");
        }else{
            try{
                ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
                modelMap.put("success", true);
                modelMap.put("shopAuthMap", shopAuthMap);
            }catch (Exception e){
                e.printStackTrace();
                logger.error("getshopauthbyid error:" + e.getMessage());
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    /**
     * 将微信回传用户信息的url放入到二维码中，并通过Response返回个前台填充到<img src/>中的src属性中
     * @param request
     * @param response
     */
    @RequestMapping(value="/generateqrcodeshopauth", method=RequestMethod.GET)
    @ResponseBody
    public void generateQRCodeShopAuth(HttpServletRequest request, HttpServletResponse response){
        // 附加上必要的店铺信息到微信回传的state中，这样才知道回传用户信息往哪个店铺添加
        Shop shop = (Shop)request.getSession().getAttribute("currentShop");
        if(shop != null && shop.getShopId() != null){
            // 获取当前时间戳，以保证二维码的时间有效性，防止他人滥用，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 加上aaa是为了方便剥离相应的信息
            String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            try{
                // 将content的信息先进行Base64编码，防止特殊字符对url进行干扰
                String longUrl = urlPrefix + authUrl + urlMiddle + URLEncoder.encode(content, "UTF-8") + urlSuffix;
                // 将目标url转为短的URL，防止微信扫码失效
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(shortUrl, response);
                // 将二维码图片流输出到响应流中
                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
            }catch(Exception e){
                e.printStackTrace();
                logger.error(e.toString());
            }
        }
    }

    /**
     * 将二维码打开的url回传的微信用户信息进行授权
     * @param request
     * @return
     */
    // todo 调试
    @RequestMapping(value="/addshopauthmap")
    public ModelAndView addShopAuthMap(HttpServletRequest request){
        ModelAndView authFail = new ModelAndView("shop/shopauthfail");
        ModelAndView authSuccess = new ModelAndView("shop/shopauthsuccess");
        // 获取店铺信息
        String state = HttpServletRequestUtil.getString(request, "state");
        String jsonStr = state.replaceAll("aaa", "\"");
        ObjectMapper objectMapper = new ObjectMapper();
        WechatInfo wechatInfo = null;
        try{
            wechatInfo = objectMapper.readValue(jsonStr, WechatInfo.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 获取回传得到的用户信息
        String code = request.getParameter("code");
        PersonInfo personInfo = null;
        try{
           UserAccessToken userAccessToken =  WechatUtil.getUserAccessToken(code);
           String openId = userAccessToken.getOpenId();
           WechatUser wechatUser = WechatUtil.getUserInfo(userAccessToken.getAccessToken(), openId);
           personInfo = WechatUtil.getPersonInfo(wechatUser);
           personInfo.setUserType(4); // 设置为店家的管理员
           // 如果openId对应用户未注册过则进行注册
           if(!checkOpenIdRegister(openId)){
               WechatAuth wechatAuth = new WechatAuth();
               wechatAuth.setOpenId(openId);
               wechatAuth.setPersonInfo(personInfo);
               WechatExecution execution = wechatAuthService.register(wechatAuth);
               if(execution.getState() != WechatAuthStateEnum.SUCCESS.getState()){
                   logger.info("error");
                   return authFail;
               }
           }else{
               // 填充personInfo的userId，后面才可以插入
               WechatAuth temp = wechatAuthService.getWechatAuthById(openId);
               personInfo = temp.getPersonInfo();
           }
        }catch (Exception e){
            logger.error(e.toString());
            return authFail;
        }

        // 检查是否重复授权或者二维码过期
        if(checkAlreadyAuth(wechatInfo.getShopId(), personInfo.getUserId()) || !vaildQRCode(wechatInfo)){
            return authFail;
        }

        if(wechatInfo != null && wechatInfo.getShopId() != null && personInfo != null && personInfo.getUserId() != null){
            try{
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);
                shopAuthMap.setEmployee(personInfo);
                ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if(shopAuthMapExecution.getState() == ShopAuthMapStateEnum.AUTH_SUCCESS.getState()){
                    return authSuccess;
                }else{
                    logger.error(shopAuthMapExecution.getStateInfo());
                    return authFail;
                }
            }catch (Exception e){
                logger.error(e.toString());
                return authFail;
            }
        }else{
            return authFail;
        }
    }

    /**
     * 检查openId是否注册过
     * @param openId
     * @return
     */
    private Boolean checkOpenIdRegister(String openId){
        WechatAuth wechatAuth = wechatAuthService.getWechatAuthById(openId);
        if(wechatAuth != null && wechatAuth.getPersonInfo() != null){
            return true;
        }
        return false;
    }

    /**
     * 检查二维码是否过期
     * @param wechatInfo
     * @return
     */
    private Boolean vaildQRCode(WechatInfo wechatInfo) {
        if (wechatInfo != null && wechatInfo.getCreateTime() != null && wechatInfo.getShopId() != null) {
            Long expireTime = wechatInfo.getCreateTime();
            Long currentTime = System.currentTimeMillis();
            // 如果二维码创建时间和现在时间超过10分钟则失效
            if (currentTime - expireTime > 600000) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 检查是否已经授权过，避免重复授权数据库一致插入
     * @param shopId
     * @param employeeId
     * @return
     */
    private Boolean checkAlreadyAuth(Long shopId, Long employeeId){
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.listShopAuthMapByShopId(shopId, 0, 100);
        List<ShopAuthMap> list = shopAuthMapExecution.getShopAuthMapList();
        for(ShopAuthMap shopAuthMap : list){
            if(shopAuthMap.getEmployee().getUserId() == employeeId){
                return true;
            }
        }
        return false;
    }

    // 检查是否为店长，如果删除或者修改的是店长那就不好了
    private boolean checkPermission(ShopAuthMap shopAuthMap){
        if(shopAuthMap != null && shopAuthMap.getTitleFlag() == 0){
            return false;
        }
        return true;
    }
}
