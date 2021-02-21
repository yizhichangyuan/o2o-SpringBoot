package com.imooc.o2o.web.wechat;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatExecution;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.service.WechatAuthService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * 微信回传数据的url：https://open.weixin.qq.com/connect/oauth2/authorize?appid={你的微信测试号appid}&redirect_url={你处理接收微信传来的用户信息的url，该项目就为http://lookstarry.com/o2o/wechatlogin/logincheck&role_type=1&response_type={request以get获取用户信息参数名，这里定义为code}&scope=snsapi_userinfo&state=1#wechat_redirect
 * 这里会获取code，之后就可以通过code获取到access_token进而获取到用户信息
 */
@Controller
@RequestMapping("/wechatlogin")
public class WechatLoginController {
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private PersonInfoDao personInfoDao;

    private static final int FRONTEND = 1;
    private static final int SHOPEND = 2;

    private static Logger logger = LoggerFactory.getLogger(WechatLoginController.class);

    @RequestMapping(value = "/logincheck", method = RequestMethod.GET)
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        int roleType = HttpServletRequestUtil.getInt(request, "state");
        logger.debug("weixin login code");
        WechatUser user = null;
        String openId = null;
        if (null != code) {
            UserAccessToken token;
            try {
                // 通过code获取access_token
                token = WechatUtil.getUserAccessToken(code);
                logger.debug("weixin login token:" + token.toString());
                // 通过token获取accessToken
                String accessToken = token.getAccessToken();
                openId = token.getOpenId();
                // 通过access_token和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken, openId);
                logger.debug("weixin log user:" + user.toString());
            } catch (Exception e) {
                logger.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // 查看该用户是否微信注册过，如果没有就注册信息，如果有就跳转
        WechatAuth wechatAuth = wechatAuthService.getWechatAuthById(openId);
        if (wechatAuth == null) {
            PersonInfo personInfo = WechatUtil.getPersonInfo(user);
            // 填充用户是店家还是顾客，通过微信连接返回的state识别
            if (roleType == FRONTEND) {
                personInfo.setUserType(1);
            } else if (roleType == SHOPEND) {
                personInfo.setUserType(2);
            }
            wechatAuth = new WechatAuth();
            wechatAuth.setPersonInfo(personInfo);
            wechatAuth.setOpenId(user.getOpenId());
            try {
                WechatExecution wechatExecution = wechatAuthService.register(wechatAuth);
                if (wechatExecution.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
                    logger.info("注册失败：" + wechatExecution.getStateInfo());
                    return null;
                }
            } catch (Exception e) {
                logger.error("注册失败: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        PersonInfo personInfo = personInfoDao.queryPersonInfoById(wechatAuth.getPersonInfo().getUserId());
        request.getSession().setAttribute("user", personInfo);
        if (roleType == FRONTEND) {
            return "frontend/index";
        } else {
            return "shop/shoplist";
        }
    }
}
