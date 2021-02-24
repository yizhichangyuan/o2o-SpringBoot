package com.imooc.o2o.web.frontend;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.service.*;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.ShortNetAddressUtil;
import com.imooc.o2o.web.ShopAdmin.ProductController;
import com.imooc.o2o.web.ShopAdmin.ShopAuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @PackageName:com.imooc.o2o.web.frontend
 * @NAME:ProductController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/23 19:56
 */
@RestController
@RequestMapping(value="/frontend")
@Configuration
public class ProductExchangeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    // 微信获取用户信息的开发者appid
    private static String appId;
    // 微信获取用户信息的api前缀
    private static String urlPrefix;
    // 微信获取用户信息的api中间部分
    private static String urlMiddle;
    // 微信获取用户信息的api后缀
    private static String urlSuffix;
    // 微信回传给的响应添加授权信息的url
    private static String productexchangeurl;

    private static Logger logger = LoggerFactory.getLogger(ShopAuthController.class);
    @Autowired
    private AwardService awardService;
    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private UserShopMapService userShopMapService;

    @Value("${wechat.appid}")
    public void setAppId(String appId) {
        ProductExchangeController.appId = appId;
    }

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        ProductExchangeController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        ProductExchangeController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        ProductExchangeController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.productexchangeurl.url}")
    public void setProductexchangeurl(String productexchangeurl) {
        ProductExchangeController.productexchangeurl = productexchangeurl;
    }

    @RequestMapping("/generateproductqr")
    public void generateProductQR(HttpServletRequest request, HttpServletResponse response){
        PersonInfo user = (PersonInfo)request.getSession().getAttribute("user");
        long productId = HttpServletRequestUtil.getLong(request, "productId");
        if(user == null || productId < 0){
            return;
        }else{
            long createTime = System.currentTimeMillis();
            String content = String.format("{aaacustomerIdaaa:%d,aaaproductIdaaa:%d,aaacreateTimeaaa:%d}", user.getUserId(), productId, createTime);
            try{
                String url = urlPrefix + productexchangeurl + urlMiddle + URLEncoder.encode(content, "utf-8") + urlSuffix;
                String shortUrl = ShortNetAddressUtil.getShortURL(url);
                BitMatrix bitMatrix = CodeUtil.generateQRCodeStream(shortUrl, response);
                MatrixToImageWriter.writeToStream(bitMatrix, "png", response.getOutputStream());
            }catch(Exception e){
                logger.error(e.toString());
                throw new RuntimeException(e);
            }
        }
    }
}
