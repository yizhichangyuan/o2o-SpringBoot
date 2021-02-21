package com.imooc.o2o.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (TbUserAwardMap)实体类
 *
 * @author makejava
 * @since 2021-02-14 16:02:00
 */
public class UserAwardMap implements Serializable {
    private static final long serialVersionUID = 289046639249453619L;

    private Integer userAwardId;

    private PersonInfo user;

    private Shop shop;

    private Award award;
    /**
     * 兑换操作员id
     */
    private PersonInfo operator;
    /**
     * 消耗积分
     */
    private Integer point;
    /**
     * 兑换时间
     */
    private Date createTime;
    /**
     * 领取时间
     */
    private Date lastEditTime;
    /**
     * 0：未领取 1：已领取
     */
    private Integer usedStatus;


    public Integer getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Integer userAwardId) {
        this.userAwardId = userAwardId;
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

    public Award getAward() {
        return award;
    }

    public void setAward(Award award) {
        this.award = award;
    }

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
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

    public Date getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(Date lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getUsedStatus() {
        return usedStatus;
    }

    public void setUsedStatus(Integer usedStatus) {
        this.usedStatus = usedStatus;
    }

}