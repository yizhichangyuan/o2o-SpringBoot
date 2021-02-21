package com.imooc.o2o.dao;

import com.imooc.o2o.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LocalAuthDao {
    /**
     * 根据用户名和密码查询用户，用于用户登陆
     *
     * @param userName
     * @param password
     * @return
     */
    LocalAuth queryLocalAuthByUserNameAndPASD(@Param("username") String userName, @Param("password") String password);

    /**
     * 根据userId查询用户
     *
     * @param userId
     * @return
     */
    LocalAuth queryLocalAuthByUserId(@Param("userId") long userId);

    /**
     * 创建用户账号和密码，让账号密码与微信可以绑定
     *
     * @param localAuth
     * @return
     */
    int insertLocalAuth(LocalAuth localAuth);

    /**
     * 根据用户提供的用户和密码来修改对应的密码
     * 这里是为了验证，验证操作是否为当前用户，防止别人篡改了密码
     * 同时userId因为是从前台session中获取，所以又多了一重验证
     *
     * @param userName
     * @param password
     * @param newPassword
     * @param userId
     * @return
     */
    int updateLocalAuth(@Param("userName") String userName, @Param("password") String password,
                        @Param("newPassword") String newPassword, @Param("userId") long userId,
                        @Param("lastEditTime") Date lastEditTime);


}
