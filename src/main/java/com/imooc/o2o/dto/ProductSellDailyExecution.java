package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.enums.ProductSellDailyStateEnum;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:ProductSellDailyExecution
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 15:08
 */
public class ProductSellDailyExecution {
    private int state;
    private String stateInfo;
    private int count;
    private ProductSellDaily productSellDaily;
    private List<ProductSellDaily> list;

    public ProductSellDailyExecution(ProductSellDailyStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public ProductSellDailyExecution(ProductSellDailyStateEnum stateEnum, ProductSellDaily productSellDaily){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.productSellDaily = productSellDaily;
    }

    public ProductSellDailyExecution(ProductSellDailyStateEnum stateEnum, List<ProductSellDaily> productSellDailyList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.list = productSellDailyList;
        this.count = productSellDailyList.size();
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProductSellDaily getProductSellDaily() {
        return productSellDaily;
    }

    public void setProductSellDaily(ProductSellDaily productSellDaily) {
        this.productSellDaily = productSellDaily;
    }

    public List<ProductSellDaily> getList() {
        return list;
    }

    public void setList(List<ProductSellDaily> list) {
        this.list = list;
    }
}
