package com.imooc.o2o.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @PackageName:com.imooc.o2o.util
 * @NAME:ShopNetAddressUtil
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/20 18:48
 */
public class ShortNetAddressUtil {
    private static Logger logger = LoggerFactory.getLogger(ShortNetAddressUtil.class);

    public static int TIME_OUT = 30 * 1000; // 30s
    public static String ENCODING = "UTF-8";
    public static String key = "6031010adac4434cc4259af7@8dcb53f0eb4acf4eac6b0a0232383e93";

    /**
     * 根据传入的url，通过访问百度短视频的接口，将其转换成短的URL，URL依然有效，是为了避免二维码url过长造成失效
     * @param originURL
     * @return
     */
    public static String getShortURL(String originURL) throws UnsupportedEncodingException {
        String tinyUrl = null;
        try{
            String encodeUrl = URLEncoder.encode(originURL, "UTF-8");
            String apiUrl = String.format("http://api.suowo.cn/api.htm?url=%s&format=json&key=%s&expireDate=2030-03-31",
                    encodeUrl,key);
            // 指定百度短视频的接口
            URL url = new URL(apiUrl);
            // 建立连接
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            // 设置连接的参数
            // 使用连接进行输出
            connection.setDoOutput(true);
            // 使用连接进行输入
            connection.setDoInput(true);
            // 不使用缓存
            connection.setUseCaches(false);
            // 设置连接超时时间为30秒
            connection.setConnectTimeout(TIME_OUT);
            // 设置请求模式为POST
            connection.setRequestMethod("GET");
            // 连接百度短视频的端口
            connection.connect();
            // 获取返回的字符串
            String responseStr = getResponseStr(connection);
            // 在字符串里获取tinyURL，即短链接
            tinyUrl = getValueByKey(responseStr, "url");
            logger.info("tinyurl:" + tinyUrl);
            // 关闭连接
            connection.disconnect();
        }catch(IOException e){
            logger.error("getshortURL error:" + e.toString());
        }
        return tinyUrl;
    }

    /**
     * 获取回传的json字符串
     * @param connection
     * @return
     * @throws IOException
     */
    private static String getResponseStr(HttpURLConnection connection) throws IOException {
        StringBuffer result = new StringBuffer();
        // 从连接中获取http状态码
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            // 如果返回的状态码是OK，那么取出连接的输入流
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
            String inputLine = "";
            while((inputLine = reader.readLine()) != null){
                result.append(inputLine);
            }
        }
        return String.valueOf(result);
    }

    /**
     * 根据json字符串以及对应的key获取相应的value
     * @param replyText
     * @param key
     * @return
     */
    private static String getValueByKey(String replyText, String key){
        ObjectMapper mapper = new ObjectMapper();
        // 定义json结点
        JsonNode node;
        String targetValue = null;
        try{
            // 把调用返回的消息传转换为json对象
            node = mapper.readTree(replyText);
            // 根据key从json对象中获取对应的值
            targetValue = node.get(key).asText();
        } catch (JsonMappingException e) {
            logger.error("getValueByKey error:" + e.toString());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            logger.error("getValueByKey error:" + e.toString());
            e.printStackTrace();
        }
        return targetValue;
    }



    public static void main(String[] args) throws UnsupportedEncodingException {
        getShortURL("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2c605206217d88b5&redirect_uri=http://115.28.159.6/cityrun/wechatlogin.action&role_type=1&response_type=code&scope=snsapi_userinfo&state=STATE123qweasd#wechat_redirect");
    }
}
