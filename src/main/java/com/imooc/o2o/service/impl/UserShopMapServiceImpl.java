package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserShopMapStateEnum;
import com.imooc.o2o.exceptions.UserShopMapException;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.HttpServletRequestUtil;
import com.imooc.o2o.util.RowIndexCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.imooc.o2o.service.impl
 * @NAME:UserShopMapServiceImpl
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 10:46
 */
@Service
public class UserShopMapServiceImpl implements UserShopMapService {
    @Autowired
    private UserShopMapDao userShopMapDao;

    /**
     * 用户购买商品或发生兑换时，更新用户积分
     * @param userShopMap
     * @return
     */
    @Override
    public UserShopMapExecution updateUserShopMap(UserShopMap userShopMap) {
        if(userShopMap == null || userShopMap.getShop() == null || userShopMap.getShop().getShopId() == null
            || userShopMap.getUser() == null || userShopMap.getUser().getUserId() == null){
            return new UserShopMapExecution(UserShopMapStateEnum.NULL_INFO);
        }
        try{
            int effectNum = userShopMapDao.updateUserShopMap(userShopMap.getShop().getShopId(), userShopMap.getUser().getUserId(), userShopMap.getPoint());
            if(effectNum <= 0){
                return new UserShopMapExecution(UserShopMapStateEnum.INNER_ERROR);
            }else{
                return new UserShopMapExecution(UserShopMapStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new UserShopMapException(e.toString());
        }
    }

    @Override
    public UserShopMapExecution addUserShopMap(UserShopMap userShopMap) {
        if(userShopMap == null || userShopMap.getShop() == null || userShopMap.getShop().getShopId() == null
                || userShopMap.getUser() == null || userShopMap.getUser().getUserId() == null || userShopMap.getPoint() != null){
            return new UserShopMapExecution(UserShopMapStateEnum.NULL_INFO);
        }
        try{
            userShopMap.setCreateTime(new Date());
            int effectNum = userShopMapDao.insertUserShopMap(userShopMap);
            if(effectNum <= 0){
                return new UserShopMapExecution(UserShopMapStateEnum.INNER_ERROR);
            }else{
                return new UserShopMapExecution(UserShopMapStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new UserShopMapException(e.toString());
        }
    }

    @Override
    public UserShopMapExecution queryUserShopMapList(UserShopMap userShopMap, int pageIndex, int pageSize) {
        if(userShopMap == null || userShopMap.getShop() == null || userShopMap.getShop().getShopId() == null){
            return new UserShopMapExecution(UserShopMapStateEnum.NULL_INFO);
        }
        if(pageIndex < -1 || pageSize < -1){
            return new UserShopMapExecution(UserShopMapStateEnum.EMPTY_PAGE_INDEX);
        }
        try{
            int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
            List<UserShopMap> userShopMapList = userShopMapDao.queryUserShopMapList(userShopMap, rowIndex, pageSize);
            int count = userShopMapDao.queryUserShopMapCount(userShopMap);
            UserShopMapExecution userShopMapExecution = new UserShopMapExecution(UserShopMapStateEnum.SUCCESS, userShopMapList);
            userShopMapExecution.setCount(count);
            return userShopMapExecution;
        }catch(Exception e){
            throw new UserShopMapException(e.toString());
        }
    }

    /**
     * 用户登陆获取用户积分
     * @param userId
     * @param shopId
     * @return
     */
    @Override
    public UserShopMapExecution queryUserShopMapById(long userId, long shopId) {
        if(userId <= -1 || shopId <= -1){
            return new UserShopMapExecution(UserShopMapStateEnum.NULL_INFO);
        }
        try{
            UserShopMap userShopMap = userShopMapDao.queryUserShopMapById(userId, shopId);
            return new UserShopMapExecution(UserShopMapStateEnum.SUCCESS, userShopMap);
        }catch(Exception e){
            throw new UserShopMapException(e.toString());
        }
    }
}
