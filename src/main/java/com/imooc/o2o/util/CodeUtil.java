package com.imooc.o2o.util;

import com.google.code.kaptcha.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CodeUtil {
    public static Boolean checkVerifyCode(HttpServletRequest request) {
        // 在shopOperation.html中img的src发送请求给/o2o/Kaptcha给Kaptcha Servlet的时候，
        // 同时图片真实的验证码就会种植在session中，便于校验用户输入与真实是否相同
        String verifyCodeExpected = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String verifyCode = HttpServletRequestUtil.getString(request, "verifyCodeActual");
        if (verifyCode == null || !verifyCode.equals(verifyCodeExpected)) {
            return false;
        }
        return true;
    }


    /**
     * 生成二维码的图片流
     * @param content
     * @param resp
     * @return BitMatrix 是一个比特的矩阵
     */
    public static BitMatrix generateQRCodeStream(String content, HttpServletResponse resp){
        // 给相应添加头部信息，主要告诉浏览器返回的是图片流
        resp.setHeader("Cache-Control", "no-store"); // 不设定缓存，因为二维码会过期
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/png"); // 告诉浏览器是文件流

        // 设置图片的文字编码和内边框距
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try{
            // 生成二维码，参数顺序分别为：编码内容（一般为url）、编码类型、生成图片宽度、生成图片宽度、设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        }catch(WriterException e){
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }
}
