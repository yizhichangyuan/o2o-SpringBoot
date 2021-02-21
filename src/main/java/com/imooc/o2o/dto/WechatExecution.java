package com.imooc.o2o.dto;

import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;

import java.util.List;

public class WechatExecution {
    private int state;

    private String stateInfo;

    private WechatAuth wechatAuth;

    private List<WechatAuth> wechatAuthList;

    public WechatExecution(WechatAuthStateEnum wechatAuthStateEnum) {
        this.state = wechatAuthStateEnum.getState();
        this.stateInfo = wechatAuthStateEnum.getStateInfo();
    }

    public WechatExecution(WechatAuthStateEnum wechatAuthStateEnum, WechatAuth wechatAuth) {
        this.state = wechatAuthStateEnum.getState();
        this.stateInfo = wechatAuthStateEnum.getStateInfo();
        this.wechatAuth = wechatAuth;
    }


    public WechatExecution(WechatAuthStateEnum wechatAuthStateEnum, List<WechatAuth> wechatAuthList) {
        this.state = wechatAuthStateEnum.getState();
        this.stateInfo = wechatAuthStateEnum.getStateInfo();
        this.wechatAuthList = wechatAuthList;
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

    public WechatAuth getWechatAuth() {
        return wechatAuth;
    }

    public void setWechatAuth(WechatAuth wechatAuth) {
        this.wechatAuth = wechatAuth;
    }

    public List<WechatAuth> getWechatAuthList() {
        return wechatAuthList;
    }

    public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
        this.wechatAuthList = wechatAuthList;
    }
}
