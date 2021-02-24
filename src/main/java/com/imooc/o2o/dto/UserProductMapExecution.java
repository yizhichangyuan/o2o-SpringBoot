package com.imooc.o2o.dto;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.enums.ProductSellDailyStateEnum;
import com.imooc.o2o.enums.UserProductMapStateEnum;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:UserProductMapExecution
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 15:21
 */
public class UserProductMapExecution {
    private int state;
    private String stateInfo;
    private int count;
    private UserProductMap userProductMap;
    private List<UserProductMap> list;

    public UserProductMapExecution(UserProductMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum, UserProductMap userProductMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userProductMap = userProductMap;
    }

    public UserProductMapExecution(UserProductMapStateEnum stateEnum, List<UserProductMap> UserProductMapList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.list = UserProductMapList;
        this.count = UserProductMapList.size();
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

    public UserProductMap getUserProductMap() {
        return userProductMap;
    }

    public void setUserProductMap(UserProductMap userProductMap) {
        this.userProductMap = userProductMap;
    }

    public List<UserProductMap> getList() {
        return list;
    }

    public void setList(List<UserProductMap> list) {
        this.list = list;
    }
}
