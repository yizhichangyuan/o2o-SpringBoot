package com.imooc.o2o.enums;

/**
 * @PackageName:com.imooc.o2o.enums
 * @NAME:UserShopMapStateEnum
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 10:40
 */
public enum UserShopMapStateEnum {
    SUCCESS(1, "成功"),NULL_INFO(-1001, "店铺id为空或用户id为空"),
    INNER_ERROR(-1002, "内部错误"), EMPTY_PAGE_INDEX(-1003, "empty pageIndex or pageSize");
    private int state;
    private String stateInfo;

    private UserShopMapStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
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
