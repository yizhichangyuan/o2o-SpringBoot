package com.imooc.o2o.service;

import com.imooc.o2o.dto.WechatExecution;
import com.imooc.o2o.entity.WechatAuth;

public interface WechatAuthService {
    WechatExecution register(WechatAuth wechatAuth);

    WechatAuth getWechatAuthById(String openId);
}
