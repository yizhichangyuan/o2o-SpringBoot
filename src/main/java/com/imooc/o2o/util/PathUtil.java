package com.imooc.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 项目图片的根路径，项目图片的子路径
 */
@Configuration // 必须添加才可以通过@Value访问application.properties下键值对
public class PathUtil {
    private static String separator = System.getProperty("file.separator");

    private static String widPath;

    private static String linuxPath;

    private static String shopPath;

    @Value("${win.base.path}")
    public void setWidPath(String widPath) {
        PathUtil.widPath = widPath;
    }

    @Value("${linux.base.path}")
    public void setLinuxPath(String linuxPath) {
        PathUtil.linuxPath = linuxPath;
    }

    @Value("${shop.relevant.path}")
    public void setShopPath(String shopPath) {
        PathUtil.shopPath = shopPath;
    }

    /**
     * 根据系统选择用户上传图片存储的根路径
     *
     * @return
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";

        // 关于图片为什么放置在其他路径而不是resources下
        // 因为应用部署的时候，之后重新部署应用项目会重新回到部署前的状态
        // 在之前部署过程中用户上传的新增的图片就会被删除
        if (os.toLowerCase().startsWith("win")) {
            basePath = widPath;
        } else {
            basePath = linuxPath;
        }

        // 路径分隔符替换为系统分隔符
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    /**
     * 根据上传的店铺id选择该店铺图片放置根路径
     *
     * @param shopId
     * @return
     */
    public static String getShopImgPath(long shopId) {
        String imagePath = shopPath + shopId + "/";
        return imagePath.replace("/", separator);
    }

    public static String getProductSimpleImgPath(long shopId, long productId) {
        String productPath = shopPath + shopId + "/" + productId + "/";
        return productPath.replaceAll("/", separator);
    }

    public static String getProductDetailImgPath(long shopId, long productId) {
        String productPath = shopPath + shopId + "/" + productId + "/detail/";
        return productPath.replaceAll("/", separator);
    }
}
