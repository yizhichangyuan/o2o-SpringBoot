package com.imooc.o2o.web.qrgenerate;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.web.frontend.AwardExchangeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @PackageName:com.imooc.o2o.web.qrgenerate
 * @NAME:QRCodeGenerateController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/24 16:58
 */
@Controller
@RequestMapping(value="qrCode")
@Configuration
public class QRCodeGenerateController {
    private static String PSD_QR_FRONTEND = "http://lookstarry.com/o2o/local/login?userType=1";
    private static String PSD_QR_SHOP_ADMIN = "http://lookstarry.com/o2o/local/login?userType=2";
    private static String WECHAT_QR_FRONTEND;
    private static String WECHAT_QR_SHOP_ADMIN;
    private static Logger logger = LoggerFactory.getLogger(QRCodeGenerateController.class);

    // 微信获取用户信息的开发者appid
    private static String appId;
    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加授权信息的url
    private static String wechatLoginUrl;

    @Value("${wechat.appid}")
    public void setAppId(String appId) {
        QRCodeGenerateController.appId = appId;
    }

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        QRCodeGenerateController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        QRCodeGenerateController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        QRCodeGenerateController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.login.url}")
    public void setWechatLoginUrl(String wechatLoginUrl) {
        QRCodeGenerateController.wechatLoginUrl = wechatLoginUrl;
    }

    @RequestMapping(value="/generateQRCode")
    public void generateQRCode(HttpServletRequest request, HttpServletResponse response){
        try{
            int status = HttpServletRequestUtil.getInt(request, "status");
            WECHAT_QR_FRONTEND = urlPrefix + wechatLoginUrl + urlMiddle + 1 + urlSuffix;
            WECHAT_QR_SHOP_ADMIN = urlPrefix + wechatLoginUrl + urlMiddle + 2 + urlSuffix;
            String content = null;
            if(status == 1){
                content = PSD_QR_FRONTEND;
            }else if(status == 2){
                content = PSD_QR_SHOP_ADMIN;
            }else if(status == 3){
                content = WECHAT_QR_FRONTEND;
                content = "http://mrw.so/6i9SeZ"; // 短链连接
//                content = ShortNetAddressUtil.getShortURL(content);
            }else if(status == 4){
                content = WECHAT_QR_SHOP_ADMIN;
                content = "http://mrw.so/6pG5DQ"; // 短链接
//                content = ShortNetAddressUtil.getShortURL(content);
            }
                BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(content, response);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
        }catch(Exception e){
            logger.error("二维码生成失败：" + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
