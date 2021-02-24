package com.imooc.o2o.enums;

/**
 * @PackageName:com.imooc.o2o.enums
 * @NAME:UserAwardMapStateEnum
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 20:27
 */
public enum UserAwardMapStateEnum {
    SUCCESS(1, "操作成功"),NULL_INFO(-1001, "信息为空"),
    UNSATISFIED_POINT(-1002, "积分不足"),
    INNER_ERROR(-1003, "内部错误");

    private int state;
    private String stateInfo;


    private UserAwardMapStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static UserAwardMapStateEnum stateOf(int state) {
        for (UserAwardMapStateEnum stateEnum : values()) {
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
