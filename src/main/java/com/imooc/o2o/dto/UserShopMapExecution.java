package com.imooc.o2o.dto;

import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserShopMapStateEnum;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:UserShopMapExecution
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 10:37
 */
public class UserShopMapExecution {
    private int state;
    private String stateInfo;
    private UserShopMap userShopMap;
    private List<UserShopMap> userShopMapList;
    private int count;

    public UserShopMapExecution(UserShopMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum, UserShopMap userShopMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMap = userShopMap;
    }

    public UserShopMapExecution(UserShopMapStateEnum stateEnum, List<UserShopMap> list){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userShopMapList = list;
        this.count = list.size();
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public UserShopMap getUserShopMap() {
        return userShopMap;
    }

    public void setUserShopMap(UserShopMap userShopMap) {
        this.userShopMap = userShopMap;
    }

    public List<UserShopMap> getUserShopMapList() {
        return userShopMapList;
    }

    public void setUserShopMapList(List<UserShopMap> userShopMapList) {
        this.userShopMapList = userShopMapList;
    }
}
