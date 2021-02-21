package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.exceptions.LocalAuthException;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
    @Autowired
    private LocalAuthDao localAuthDao;

    private Logger logger = LoggerFactory.getLogger(LocalAuthServiceImpl.class);

    @Override
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) {
        if (localAuth == null || localAuth.getUserName() == null || localAuth.getPassword() == null
                || localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        // 首先查看用户是否已经绑定了平台账户，已经绑定则提示已经绑定
        LocalAuth temp = localAuthDao.queryLocalAuthByUserId(localAuth.getPersonInfo().getUserId());
        if (temp != null) {
            return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
        }
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        localAuth.setPassword(MD5.getMd5(localAuth.getPassword())); // 将用户密码加密后入库
        try {
            int effectNum = localAuthDao.insertLocalAuth(localAuth);
            if (effectNum <= 0) {
                logger.error("insert localAuth fail");
                throw new LocalAuthException("bindLocalAuth error");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
            }
        } catch (Exception e) {
            logger.error("insert localAuth fail:" + e.getMessage());
            throw new LocalAuthException("bindLocalAuth error");
        }
    }

    @Override
    public LocalAuthExecution modifyPassword(String username, String password,
                                             String newPassword, long userId) {
        if (username == null || password == null || userId == -1L || newPassword == null || newPassword.equals(password)) {
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
        try {
            password = MD5.getMd5(password);
            newPassword = MD5.getMd5(newPassword);
            int effectNum = localAuthDao.updateLocalAuth(username, password, newPassword, userId, new Date());
            if (effectNum <= 0) {
                logger.error("update localAuth fail");
                throw new LocalAuthException("账号密码修改失败");
            } else {
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            logger.error("modifyPassword error:" + e.getMessage());
            e.printStackTrace();
            throw new LocalAuthException("账号密码修改失败：" + e.getMessage());
        }
    }

    @Override
    public LocalAuth queryLocalAuthById(long userId) {
        return localAuthDao.queryLocalAuthByUserId(userId);
    }

    @Override
    public LocalAuth loginByLocalAuth(String username, String password) {
        return localAuthDao.queryLocalAuthByUserNameAndPASD(username, MD5.getMd5(password));
    }
}
