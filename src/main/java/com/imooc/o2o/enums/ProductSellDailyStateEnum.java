package com.imooc.o2o.enums;

public enum ProductSellDailyStateEnum {
    SUCCESS(1, "操作成功"),NULL_SHOP_Id(-1, "店铺id为空"),INNER_ERROR(-1001, "内部错误");
    private int state;
    private String stateInfo;

    private ProductSellDailyStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static ProductSellDailyStateEnum stateOf(int state){
        for(ProductSellDailyStateEnum temp : values()){
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
