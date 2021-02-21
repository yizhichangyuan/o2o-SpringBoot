package com.imooc.o2o.enums;

public enum ProductCategoryStateEnum {
    SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "内部发生错误"),
    NULL_PRODUCT_CATEGORY(-1002, "产品类别信息为空"), CREATE_FAIL(-2, "商品类别创建失败");

    private String stateInfo;
    private int state;

    public String getStateInfo() {
        return stateInfo;
    }

    public int getState() {
        return state;
    }

    private ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public ProductCategoryStateEnum getEnum(int state) {
        for (ProductCategoryStateEnum productCategoryStateEnum : values()) {
            if (productCategoryStateEnum.getState() == state) {
                return productCategoryStateEnum;
            }
        }
        return null;
    }
}
