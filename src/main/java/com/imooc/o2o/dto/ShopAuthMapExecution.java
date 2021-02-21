package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:ShopAuthMapExecution
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/19 21:10
 */
public class ShopAuthMapExecution {
    private int state;

    private String stateInfo;

    private int count;

    private ShopAuthMap shopAuthMap;

    private List<ShopAuthMap> shopAuthMapList;

    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, ShopAuthMap shopAuthMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMap = shopAuthMap;
    }

    public ShopAuthMapExecution(ShopAuthMapStateEnum stateEnum, List<ShopAuthMap> list){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.shopAuthMapList = list;
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

    public ShopAuthMap getShopAuthMap() {
        return shopAuthMap;
    }

    public void setShopAuthMap(ShopAuthMap shopAuthMap) {
        this.shopAuthMap = shopAuthMap;
    }

    public List<ShopAuthMap> getShopAuthMapList() {
        return shopAuthMapList;
    }

    public void setShopAuthMapList(List<ShopAuthMap> shopAuthMapList) {
        this.shopAuthMapList = shopAuthMapList;
    }

}
