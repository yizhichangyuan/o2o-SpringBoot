package com.imooc.o2o.enums;

public enum LocalAuthStateEnum {
    LOGIN_FAIL(-1, "密码或帐号输入有误"), SUCCESS(0, "操作成功"), NULL_AUTH_INFO(-1006,
            "注册信息为空"), ONLY_ONE_ACCOUNT(-1007, "最多只能绑定一个本地帐号");

    private int state;
    private String stateInfo;

    private LocalAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public LocalAuthStateEnum stateOf(int state) {
        for (LocalAuthStateEnum localAuthStateEnum : values()) {
            if (localAuthStateEnum.getState() == state) {
                return localAuthStateEnum;
            }
        }
        return null;
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
}
