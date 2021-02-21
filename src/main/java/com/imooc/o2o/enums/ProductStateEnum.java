package com.imooc.o2o.enums;

public enum ProductStateEnum {
    SUCCESS(1, "操作成功"), NULL_PRODUCT(-1002, "商品信息为空"),
    INNER_ERROR(-1001, "内部错误");
    private int state;
    private String stateInfo;

    private ProductStateEnum() {
    }

    private ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static ProductStateEnum stateOf(int state) {
        for (ProductStateEnum productStateEnum : values()) {
            if (productStateEnum.getState() == state) {
                return productStateEnum;
            }
        }
        return null;
    }
}
