package com.imooc.o2o.util.wechat;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.o2o.dto.UserAccessToken;
import com.imooc.o2o.dto.WechatUser;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.util.DESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;

@Configuration
public class WechatUtil {
    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);

    public static String appId;

    private static String appSecret;


//    @Value("${wechat.appid}")
//    public void setAppId(String appId) {
//        WechatUtil.appId = appId;
//    }
//
//    @Value("${wechat.appsecret}")
//    public void setAppsecret(String appsecret) {
//        WechatUtil.appSecret = appsecret;
//    }

    public static UserAccessToken getUserAccessToken(String code) {
        // 测试号信息里的appId
        String appId = "wx6d9e79f7e8844954";
        logger.debug("appId:" + appId);
        // 测试好信息里的appsecret
        String appsecret = "eb1dd891dffa3d6a4154d0031e1f3dea";
        logger.debug("secret:" + appsecret);
        // 根据传入的code，拼接出访问微信定义好的接口URL
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret="
                + appsecret + "&code=" + code + "&grant_type=authorization_code";
        // 向相应的URL发送请求获取token json字符串
        String tokenStr = httpsRequest(url, "GET", null);
        logger.debug("userAccessToken:" + tokenStr);
        UserAccessToken token = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 将json字符串转换为响应的对象
            token = objectMapper.readValue(tokenStr, UserAccessToken.class);
        } catch (JsonParseException e) {
            logger.error("获取用户accessToken失败：" + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户accessToken失败：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户accessToken失败：" + e.getMessage());
            e.printStackTrace();
        }
        if (token == null) {
            logger.error("获取用户accessToken失败");
            return null;
        }
        return token;
    }

    public static WechatUser getUserInfo(String accessToken, String openId) {
        // 根据传入的accessToken以及openId拼接出访问微信定义的端口并获取用户信息的url
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken
                + "&openid=" + openId + "&lang=zh_CN";
        // 访问该url获取用户信息json字符串
        String userStr = httpsRequest(url, "GET", null);
        logger.debug("user info:" + userStr);
        WechatUser user = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user = objectMapper.readValue(userStr, WechatUser.class);
        } catch (JsonParseException e) {
            logger.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("获取用户信息失败：" + e.getMessage());
            e.printStackTrace();
        }
        if (user == null) {
            logger.error("获取用户信息失败");
            return null;
        }
        return user;
    }

    public static PersonInfo getPersonInfo(WechatUser wechatUser) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setGender(wechatUser.getSex() + "");
        personInfo.setName(wechatUser.getNickName());
        personInfo.setProfileImg(wechatUser.getHeadimgurl());
        return personInfo;
    }

    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们置顶的信任管理器初始化（微信要求必须用https加密方式发送请求）
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSlSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST)
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equals(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换为字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferReader.close();
            inputStreamReader.close();
            inputStream.close();
            httpUrlConn.disconnect();
            logger.debug("https buffer:" + buffer.toString());
        } catch (ConnectException e) {
            logger.error("Weixin server connection time out.");
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return buffer.toString();
    }
}
