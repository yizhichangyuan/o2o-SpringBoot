package com.imooc.o2o.service;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.exceptions.LocalAuthException;

public interface LocalAuthService {
    /**
     * 根据localAuth平台账号绑定用户微信账号
     *
     * @param localAuth
     * @return
     */
    LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthException;

    /**
     * 修改用户账号的密码，其中userId是为了做验证，从session中获取，防止他人篡改
     *
     * @param username
     * @param password
     * @param newPassword
     * @param userId
     * @return
     */
    LocalAuthExecution modifyPassword(String username, String password, String newPassword, long userId) throws LocalAuthException;

    /**
     * 根据userId查询用户的基本信息，主要用来检查用户是否已经绑定过平台账号
     *
     * @param userId
     * @return
     */
    LocalAuth queryLocalAuthById(long userId);

    /**
     * 根据用户给定的用户密码进行登陆，这里的password是明文，对比数据库已经经过MD5加密的密码
     *
     * @param username
     * @param password
     * @return
     */
    LocalAuth loginByLocalAuth(String username, String password);
}
