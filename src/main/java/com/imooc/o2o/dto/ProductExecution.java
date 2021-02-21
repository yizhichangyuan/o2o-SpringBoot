package com.imooc.o2o.dto;

import com.imooc.o2o.entity.Product;
import com.imooc.o2o.enums.ProductStateEnum;

import java.util.List;

public class ProductExecution {
    private int state;
    private String stateInfo;
    private Product product;
    private List<Product> productList;
    private int count;

    public ProductExecution() {
    }

    public ProductExecution(ProductStateEnum productStateEnum) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
    }

    public ProductExecution(ProductStateEnum productStateEnum, Product product) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProductExecution(ProductStateEnum productStateEnum, List<Product> productList) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.productList = productList;
        this.count = productList.size();
    }
}
