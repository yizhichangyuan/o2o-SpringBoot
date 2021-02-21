package com.imooc.o2o.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (TbUserProductMap)实体类
 *
 * @author makejava
 * @since 2021-02-14 16:02:17
 */
public class UserProductMap implements Serializable {
    private static final long serialVersionUID = -39120719081820686L;

    private Integer userProductId;

    private PersonInfo user;

    private Shop shop;

    private Product product;

    private PersonInfo operator;

    private Date createTime;
    /**
     * 购买商品获得积分
     */
    private Integer point;


    public Integer getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(Integer userProductId) {
        this.userProductId = userProductId;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PersonInfo getOperator() {
        return operator;
    }

    public void setOperator(PersonInfo operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

}