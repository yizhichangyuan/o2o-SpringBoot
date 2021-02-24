package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.UserAwardMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserAwardMapExecution;
import com.imooc.o2o.dto.UserShopMapExecution;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.UserAwardMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserAwardMapStateEnum;
import com.imooc.o2o.enums.UserShopMapStateEnum;
import com.imooc.o2o.exceptions.UserAwardMapException;
import com.imooc.o2o.service.UserAwardMapService;
import com.imooc.o2o.service.UserShopMapService;
import com.imooc.o2o.util.RowIndexCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @PackageName:com.imooc.o2o.service.impl
 * @NAME:UserAwardMapServiceImpl
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/22 20:33
 */
@Service
public class UserAwardMapServiceImpl implements UserAwardMapService {
    @Autowired
    private UserAwardMapDao userAwardMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;
    @Autowired
    private UserShopMapService userShopMapService;

    private static Logger logger = LoggerFactory.getLogger(UserAwardMapServiceImpl.class);

    @Override
    public UserAwardMapExecution queryUserAwardMapList(UserAwardMap userAwardMap, int pageIndex, int pageSize) {
        if((userAwardMap.getUser() == null || userAwardMap.getUser().getUserId() == null) &&
                (userAwardMap.getShop() == null || userAwardMap.getShop().getShopId() == null)){
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_INFO);
        }
        try{
            int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
            List<UserAwardMap> userAwardMapList = userAwardMapDao.queryUserAwardList(userAwardMap, rowIndex, pageSize);
            int count = userAwardMapDao.queryUserAwardCount(userAwardMap);
            UserAwardMapExecution userAwardMapExecution = new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS, userAwardMapList);
            userAwardMapExecution.setCount(count);
            return userAwardMapExecution;
        }catch(Exception e){
            throw new UserAwardMapException(e.toString());
        }
    }

    /**
     * 用户点击领取时，当时还未发生兑换，没有operator_id所以为null，此外需要判断用户的积分是否足够，此外是否为黑名单用户
     * @param userAwardMap
     * @return
     */
    @Transactional
    @Override
    public UserAwardMapExecution addUserAwardMap(UserAwardMap userAwardMap) {
        if(userAwardMap.getAward() == null || userAwardMap.getAward().getAwardId() == null ||
            userAwardMap.getShop() == null || userAwardMap.getShop().getShopId() == null
            || userAwardMap.getUser() == null || userAwardMap.getUser().getUserId() == null){
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_INFO);
        }
        try{
            // 如果该奖品需要积分进行兑换
            if(userAwardMap.getPoint() != null && userAwardMap.getPoint() >= 0){
                // 判断该用户是否有足够积分，查询该用户在该店铺积分
                UserShopMapExecution userShopMapExecution = userShopMapService.queryUserShopMapById(userAwardMap.getUser().getUserId(), userAwardMap.getShop().getShopId());
                if(userShopMapExecution.getState() == UserShopMapStateEnum.SUCCESS.getState()){
                    UserShopMap userShopMap = userShopMapExecution.getUserShopMap();
                    // 如果用户已经入会且积分足够兑换
                    if(userShopMap != null && userShopMap.getPoint() >= userAwardMap.getPoint()){
                        // 进行领取，当时还未进行兑换，所以操作员为空入库，当店员扫描时，再进行更新
                        PersonInfo operator = new PersonInfo();
                        operator.setUserId(null);
                        userAwardMap.setOperator(operator);
                        // 奖品还未发生兑换，所以usedStatus设为0，表示未兑换但已经领取
                        userAwardMap.setUsedStatus(0);
                        int effectNum = userAwardMapDao.insertUserAward(userAwardMap);
                        if(effectNum < 0){
                            throw new UserAwardMapException("插入tb_user_award_map失败");
                        }else{
                            // 用户积分更新
                            userShopMap.setPoint(userShopMap.getPoint() - userAwardMap.getPoint());
                            effectNum = userShopMapDao.updateUserShopMap(userShopMap.getShop().getShopId(), userShopMap.getUser().getUserId(), userShopMap.getPoint());
                            if(effectNum < 0){
                                throw new UserAwardMapException("更新tb_shop_user_map失败");
                            }
                        }
                        return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS);
                    }else{
                        // 积分不足
                        return new UserAwardMapExecution(UserAwardMapStateEnum.UNSATISFIED_POINT);
                    }
                }else{
                    return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
                }
            }else{
                // 如果不需要积分对换，则插入一条数据
                PersonInfo operator = new PersonInfo();
                operator.setUserId(null);
                userAwardMap.setOperator(operator);
                // 奖品还未发生兑换，所以usedStatus设为0，表示未兑换但已经领取
                userAwardMap.setUsedStatus(0);
                userAwardMapDao.insertUserAward(userAwardMap);
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS);
            }
        }catch(Exception e){
            logger.error("积分对换失败：" + e.getMessage());
            throw new UserAwardMapException(e.toString());
        }
    }


    /**
     * 用户初始兑换二维码，操作员进行扫描，首先检查是否违规操作（例如是否为黑名单人员），
     * 如果有设置库存还应检查库存是否足够，更新用户已领取状态
     * @param userAwardMap
     * @return
     */
    @Override
    public UserAwardMapExecution modifyUserAwardMap(UserAwardMap userAwardMap) {
        if(userAwardMap == null || userAwardMap.getUserAwardId() == null
                || userAwardMap.getUser() == null || userAwardMap.getUser().getUserId() == null
                || userAwardMap.getOperator() == null || userAwardMap.getOperator().getUserId() == null){
            return new UserAwardMapExecution(UserAwardMapStateEnum.NULL_INFO);
        }
        try{
            // 修改对应商品为已兑换，同时记录操作员的id，此前未领取时的操作员id为null
            int effectNum = userAwardMapDao.updateUserAward(userAwardMap.getUser().getUserId(), userAwardMap.getUserAwardId(), new Date(), 1, userAwardMap.getOperator().getUserId());
            if(effectNum >= 0){
                return new UserAwardMapExecution(UserAwardMapStateEnum.SUCCESS);
            }else{
                return new UserAwardMapExecution(UserAwardMapStateEnum.INNER_ERROR);
            }
        }catch(Exception e){
            throw new UserAwardMapException(e.getMessage());
        }
    }

    @Override
    public UserAwardMap queryUserAwardMapById(Long userAwardId) {
        return userAwardMapDao.queryUserAwardById(userAwardId);
    }
}
