package com.imooc.o2o.enums;

public enum AwardStateEnum {
    SUCCESS(1, "操作成功"),
    UNSATISFIED_POINT(-1001, "积分不足"),
    NULL_SHOP_ID(-1002, "店铺id为空"),
    NULL_AWARD_INFO(-1004, "奖品信息为空"),
    INNER_ERROR(-1003, "内部错误");

    private int state;
    private String stateInfo;


    private AwardStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static AwardStateEnum stateOf(int state) {
        for (AwardStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
