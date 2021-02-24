package com.imooc.o2o.enums;

public enum UserProductMapStateEnum {
    SUCCESS(1, "操作成功"), NULL_INFO(-1001, "查询条件不能为空"), INNER_ERROR(-1002, "内部错误");
    private int state;
    private String stateInfo;

    private UserProductMapStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public static UserProductMapStateEnum stateOf(int state){
        for(UserProductMapStateEnum temp : values()){
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
