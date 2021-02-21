package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.PersonInfoDao;
import com.imooc.o2o.dao.WechatAuthDao;
import com.imooc.o2o.dto.WechatExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.WechatAuth;
import com.imooc.o2o.enums.WechatAuthStateEnum;
import com.imooc.o2o.exceptions.WechatAuthExecption;
import com.imooc.o2o.service.WechatAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    @Autowired
    private WechatAuthDao wechatAuthDao;
    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    @Transactional
    public WechatExecution register(WechatAuth wechatAuth) {
        if (wechatAuth == null || wechatAuth.getOpenId() == null) {
            return new WechatExecution(WechatAuthStateEnum.LOGIN_FAIL);
        }
        PersonInfo personInfo = wechatAuth.getPersonInfo();
        if (personInfo == null) {
            return new WechatExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            personInfo.setEnableStatus(1);
            personInfo.setCreateTime(new Date());
            personInfo.setLastEditTime(new Date());
            int effectNum = personInfoDao.insertPersonInfo(personInfo);
            if (effectNum <= 0) {
                throw new WechatAuthExecption("注册用户信息失败");
            }
        } catch (Exception e) {
            throw new WechatAuthExecption("register error:" + e.getMessage());
        }

        try {
            wechatAuth.setCreateTime(new Date());
            int effectNum = wechatAuthDao.insertWechatAuth(wechatAuth);
            if (effectNum <= 0) {
                throw new WechatAuthExecption("插入wechatAuth失败");
            }
        } catch (Exception e) {
            throw new WechatAuthExecption("register error:" + e.getMessage());
        }
        return new WechatExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
    }

    @Override
    public WechatAuth getWechatAuthById(String openId) {
        return wechatAuthDao.queryWechatAuthById(openId);
    }
}
