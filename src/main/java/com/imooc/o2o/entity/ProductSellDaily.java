package com.imooc.o2o.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * (TbProductSellDaily)实体类，统计店铺每日某件商品统计  unique key（productId,shopId,create_time)
 *
 * @author makejava
 * @since 2021-02-14 16:01:19
 */
public class ProductSellDaily implements Serializable {
    private static final long serialVersionUID = -16561799001498426L;

    private Product Product;

    private Shop shop;

    /**
     * 该商品的日销量
     */
    private Integer total;

    /**
     * 统计日期，每天统计一次
     */
    private Date days;

    public com.imooc.o2o.entity.Product getProduct() {
        return Product;
    }

    public void setProduct(com.imooc.o2o.entity.Product product) {
        Product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Date getDays() {
        return days;
    }

    public void setDays(Date days) {
        this.days = days;
    }
}