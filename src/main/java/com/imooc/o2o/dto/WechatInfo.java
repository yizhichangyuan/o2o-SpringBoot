package com.imooc.o2o.dto;

/**
 * @PackageName:com.imooc.o2o.dto
 * @NAME:WechatInfo
 * @Description: 二维码传回来的state的dto
 * @author: yizhichangyuan
 * @date:2021/2/20 23:11
 */
public class WechatInfo {
    private Long shopId; // 对应添加哪个店铺员工的授权
    private Long createTime; // 二维码的过期时间，加以校验二维码是否过期
    private Long userAwardId; // 用户积分奖品usedStatus修改
    private Long userProductId; // 用户消费商品是否消费
    private Long customerId; // 辅助效验用户身份

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUserAwardId() {
        return userAwardId;
    }

    public void setUserAwardId(Long userAwardId) {
        this.userAwardId = userAwardId;
    }

    public Long getUserProductId() {
        return userProductId;
    }

    public void setUserProductId(Long userProductId) {
        this.userProductId = userProductId;
    }
}
