package com.imooc.o2o.util.wechat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SignUtil {
    // 与微信开发者中设置的token要一致
    private static String token = "o2o";

    /**
     * 验证微信api发过来的签名，需要对三者进行排序，并拼接成一个字符串，然后进行sha1加密，然后与signature进行对比
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            stringBuilder.append(arr[i]);
        }
        MessageDigest md = null;
        String tempStr = "";
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个字符串拼接成一个并进行SHA-1加密
            byte[] digest = md.digest(stringBuilder.toString().getBytes());
            tempStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        stringBuilder = null;
        // 将sha1加密后的字符串与微信发送的签名进行比较，标识该请求来源于微信
        return tempStr != null ? tempStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }


}
