package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.UserProductMapDao;
import com.imooc.o2o.dao.UserShopMapDao;
import com.imooc.o2o.dto.UserProductMapExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.UserProductMap;
import com.imooc.o2o.entity.UserShopMap;
import com.imooc.o2o.enums.UserProductMapStateEnum;
import com.imooc.o2o.exceptions.UserProductMapException;
import com.imooc.o2o.service.UserProductMapService;
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
 * @NAME:UserProductMapServiceImpl
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 15:27
 */
@Service
public class UserProductMapServiceImpl implements UserProductMapService {
    @Autowired
    private UserProductMapDao userProductMapDao;
    @Autowired
    private UserShopMapDao userShopMapDao;
    @Autowired
    private ProductDao productDao;
    private static Logger logger = LoggerFactory.getLogger(UserProductMapServiceImpl.class);

    @Override
    public UserProductMapExecution queryUserProductMapList(UserProductMap condition, int pageIndex, int pageSize) {
        // 查询条件为空，就查询了所有用户的信息
        if(condition == null || pageIndex <= -1 || pageSize <= -1){
            return new UserProductMapExecution(UserProductMapStateEnum.NULL_INFO);
        }
        try{
            int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
            List<UserProductMap> list = userProductMapDao.queryUserProductList(condition, rowIndex, pageSize);
            int count = userProductMapDao.queryUserProductCount(condition);
            UserProductMapExecution execution = new UserProductMapExecution(UserProductMapStateEnum.SUCCESS, list);
            execution.setCount(count);
            return execution;
        }catch(Exception e){
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户消费被扫码给予商品后，添加用户的一条消费记录
     * 如果该商品能够得到积分，且用户没有入会，则增加用户的入会记录
     * 如果用户已经入会，则更新用户的积分
     * @param userProductMap
     * @return
     */
    @Override
    @Transactional
    public UserProductMapExecution addUserProductMap(UserProductMap userProductMap) {
        // 数据库入库时要求用户、店铺、商品、操作员不能为空
        if (userProductMap == null || userProductMap.getUser() == null || userProductMap.getUser().getUserId() == null
                || userProductMap.getShop() == null || userProductMap.getShop().getShopId() == null
                || userProductMap.getProduct() == null || userProductMap.getProduct().getProductId() == null
                || userProductMap.getOperator() == null || userProductMap.getOperator().getUserId() == null) {

        }
        Product product = productDao.getProductById(userProductMap.getProduct().getProductId());

        // 不管有没有积分，都要新增一条消费记录
        userProductMap.setPoint(product.getPoint());
        userProductMap.setCreateTime(new Date());
        int effectNum = userProductMapDao.insertUserProduct(userProductMap);

        if (effectNum > 0) {
            // 积分更新，查看是否会让积分改变
            if (product.getPoint() != null) {
                // 查询该用户是否已经入会
                UserShopMap userShopMap = userShopMapDao.queryUserShopMapById(userProductMap.getUser().getUserId(),
                        userProductMap.getShop().getShopId());
                // 没有入会，就插入一条入会记录
                if (userShopMap == null) {
                    userShopMap = new UserShopMap();
                    userShopMap.setShop(userProductMap.getShop());
                    userShopMap.setUser(userProductMap.getUser());
                    userShopMap.setPoint(product.getPoint());
                    userShopMap.setCreateTime(new Date());
                    effectNum = userShopMapDao.insertUserShopMap(userShopMap);
                    if (effectNum <= 0) {
                        throw new UserProductMapException("新增入会失败");
                    } else {
                        return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS);
                    }
                } else {
                    // 已经入会，就更新积分
                    userShopMap.setPoint(userShopMap.getPoint() + product.getPoint());
                    effectNum = userShopMapDao.updateUserShopMap(userShopMap.getShop().getShopId(),
                            userShopMap.getUser().getUserId(), userShopMap.getPoint());
                    if (effectNum <= 0) {
                        throw new UserProductMapException("修改总积分失败");
                    } else {
                        return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS);
                    }
                }
            } else {
                return new UserProductMapExecution(UserProductMapStateEnum.SUCCESS);
            }
        }else{
            throw new UserProductMapException("新增消费记录失败");
        }
    }
}