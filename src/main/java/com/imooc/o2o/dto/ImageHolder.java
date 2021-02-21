package com.imooc.o2o.dto;

import java.io.InputStream;

/**
 * 重构，因为上传图片和名字肯定同时需要，避免参数无法一一对应，设计了该类
 */
public class ImageHolder {
    private InputStream image;
    private String imageName;

    public ImageHolder() {
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    private String imageDesc;
    private int priority;

    public ImageHolder(InputStream image, String imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
