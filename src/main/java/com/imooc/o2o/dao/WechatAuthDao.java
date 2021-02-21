package com.imooc.o2o.dao;

import com.imooc.o2o.entity.WechatAuth;
import org.springframework.stereotype.Repository;

@Repository
public interface WechatAuthDao {

    /**
     * 将微信用户数据插入，中间会关联PersonInfo
     *
     * @param wechatAuth
     * @return
     */
    int insertWechatAuth(WechatAuth wechatAuth);


    /**
     * 根据openId查询微信授权用户信息
     *
     * @param openId
     * @return
     */
    WechatAuth queryWechatAuthById(String openId);

}
