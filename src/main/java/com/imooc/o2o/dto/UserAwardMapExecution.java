package com.imooc.o2o.dto;

import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;

import java.util.List;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:UserAwardMapExecution
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 20:25
 */
public class UserAwardMapExecution {
    private int state;
    private String stateInfo;
    private int count;
    private UserAwardMap userAwardMap;
    private List<UserAwardMap> list;

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, UserAwardMap userAwardMap){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.userAwardMap = userAwardMap;
    }

    public UserAwardMapExecution(UserAwardMapStateEnum stateEnum, List<UserAwardMap> UserAwardMapList){
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.list = UserAwardMapList;
        this.count = UserAwardMapList.size();
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

    public UserAwardMap getUserAwardMap() {
        return userAwardMap;
    }

    public void setUserAwardMap(UserAwardMap userAwardMap) {
        this.userAwardMap = userAwardMap;
    }

    public List<UserAwardMap> getList() {
        return list;
    }

    public void setList(List<UserAwardMap> list) {
        this.list = list;
    }
}
