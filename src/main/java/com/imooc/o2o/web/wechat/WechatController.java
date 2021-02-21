package com.imooc.o2o.web.wechat;

import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.wechat.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信用来验证网站是否和openId绑定的路由
 */
@Controller
@RequestMapping(value = "/wechat")
public class WechatController {
    private static Logger logger = LoggerFactory.getLogger(WechatController.class);

    @RequestMapping(method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String signature = HttpServletRequestUtil.getString(request, "signature");
        String timestamp = HttpServletRequestUtil.getString(request, "timestamp");
        String nonce = HttpServletRequestUtil.getString(request, "nonce");
        // 随机字符串
        String echoStr = HttpServletRequestUtil.getString(request, "echostr");

        //通过对signature进行校验，如果成功则原封不动输出echoStr，失败则什么都不输出
        PrintWriter out = null;
        try {
            // 从response中接收到输出实例，是输出到响应流中的
            out = response.getWriter();
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                logger.debug("weixin get success...");
                out.println(echoStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
