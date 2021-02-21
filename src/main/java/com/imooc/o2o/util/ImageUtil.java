package com.imooc.o2o.util;

import com.imooc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    //水印图片在项目路径下
    private static String basePath;

    static {
        try {
//            basePath = URLDecoder.decode(System.getProperty("user.dir") + "/src/main/resources/watermark.jpg", "utf-8");
            basePath = URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("watermark.jpg").getPath(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    /**
     * @param thumbnail  用户上传文件
     * @param targetAddr 相对路径，是与店铺id相关的相对路径，可以通过PathUtil中的getImagePath()
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) throws IOException {
        String fileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr); //传入包括店铺
        File filePath = new File(PathUtil.getImgBasePath() + targetAddr + fileName + extension);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath)), 0.2f)
                    .outputQuality(0.8f).toFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetAddr + fileName + extension;
    }

    public static String saveDetailImg(ImageHolder detailImg, String targetAddr) throws IOException {
        String fileName = getRandomFileName();
        String extension = getFileExtension(detailImg.getImageName());
        makeDirPath(targetAddr);
        File filePath = new File(PathUtil.getImgBasePath() + targetAddr + fileName + extension);
        try {
            Thumbnails.of(detailImg.getImage()).size(337, 400).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath)), 0.2f)
                    .outputQuality(0.9f).toFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetAddr + fileName + extension;
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        int ranNum = random.nextInt(89999) + 10000;
        String nowTimeStr = dateFormat.format(new Date());
        return nowTimeStr + ranNum;
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param originalFileName 文件名
     * @return 扩展名
     */
    private static String getFileExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 创建目标路径所涉及的目录，即/home/work/yizhichangyuan/xxx.jpg
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件路径则删除该文件
     * 如果storePath是目录路径则删除该目录下的所有文件
     *
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if (fileOrPath.exists()) {
            // 如果是路径，删除路径下所有文件
            if (fileOrPath.isDirectory()) {
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            // 最后删除这个目录
            fileOrPath.delete();
        }
    }


    public static void main(String[] args) throws IOException {
        //通过Thread获取resources文件夹的绝对路径
        System.out.println("****");
        String basePath = Thread.currentThread().getContextClassLoader().getResource("watermark.jpg").getPath();
//                System.getProperty("user.dir");
        System.out.println(basePath);

        //尺寸剪辑、水印位置、水印来源、透明度、0.8f为压缩
//        Thumbnails.of(new File("/Users/yizhichangyuan/Pictures/-1.jpeg"))
//                .size(200, 200).watermark(Positions.BOTTOM_RIGHT,
//                ImageIO.read(new File(basePath + "/src/main/resources/watermark.jpg")),
//                0.25f).outputQuality(0.8f).toFile("/Users/yizhichangyuan/Pictures/-1new.jpeg");
    }
}
