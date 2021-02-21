package com.imooc.o2o.enums;

public enum ShopAuthMapStateEnum {
    AUTH_SUCCESS(1, "操作成功"),INNER_ERROR(-1001, "操作失败"),
    NULL_SHOP_AUTH_ID(-1002, "传入shopAuthId为空"),
    NULL_SHOP_AUTH_INFO(-1003, "传入空的授权信息");

    private int state;

    private String stateInfo;

    private ShopAuthMapStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ShopAuthMapStateEnum stateOf(int state){
        for(ShopAuthMapStateEnum temp : values()){
            if(temp.getState() == state){
                return temp;
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
