package com.imooc.o2o.dto;

import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;

public class LocalAuthExecution {
    private int state;
    private String stateInfo;
    private LocalAuth localAuth;

    public LocalAuthExecution() {

    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
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

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    public LocalAuthExecution(LocalAuthStateEnum localAuthStateEnum, LocalAuth localAuth) {
        this.state = localAuthStateEnum.getState();
        this.stateInfo = localAuthStateEnum.getStateInfo();
        this.localAuth = localAuth;
    }


}
