package com.imooc.o2o.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (TbShopAuthMap)实体类，记录店铺店员的权限表，比如是否有扫码兑换的权利
 *
 * @author makejava
 * @since 2021-02-14 16:01:42
 */
public class ShopAuthMap implements Serializable {
    private static final long serialVersionUID = -39429151888666665L;

    private Integer shopAuthId;

    private Shop shop;

    private PersonInfo employee;

    /**
     * 职称
     */
    private String title;

    /**
     * 职称符号，保留字段，用于权限控制，例如0表示店家，1扫码兑换的权利
     */
    private Integer titleFlag;

    private Date createTime;

    private Date lastEditTime;
    /**
     * 0：无效，1：有效
     */
    private Integer enableStatus;


    public Integer getShopAuthId() {
        return shopAuthId;
    }

    public void setShopAuthId(Integer shopAuthId) {
        this.shopAuthId = shopAuthId;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public PersonInfo getEmployee() {
        return employee;
    }

    public void setEmployee(PersonInfo employee) {
        this.employee = employee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTitleFlag() {
        return titleFlag;
    }

    public void setTitleFlag(Integer titleFlag) {
        this.titleFlag = titleFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

}