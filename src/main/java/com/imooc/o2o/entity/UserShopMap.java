package com.imooc.o2o.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (TbUserShopMap)实体类
 *
 * @author makejava
 * @since 2021-02-14 16:02:32
 */
public class UserShopMap implements Serializable {
    private static final long serialVersionUID = -93250734907186479L;

    private Integer userShopId;

    private PersonInfo user;

    private Shop shop;
    /**
     * 用户总积分
     */
    private Integer point;

    private Date createTime;


    public Integer getUserShopId() {
        return userShopId;
    }

    public void setUserShopId(Integer userShopId) {
        this.userShopId = userShopId;
    }

    public PersonInfo getUser() {
        return user;
    }

    public void setUser(PersonInfo user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}