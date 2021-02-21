package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopAuthMapDao;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.exceptions.ShopAuthMapOperationException;
import com.imooc.o2o.service.ShopAuthMapService;
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
 * @NAME:ShopAuthMapServiceImpl
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/19 21:21
 */
@Service
public class ShopAuthMapServiceImpl implements ShopAuthMapService {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    private Logger logger = LoggerFactory.getLogger(ShopAuthMapServiceImpl.class);

    @Override
    public ShopAuthMapExecution listShopAuthMapByShopId(Long shopId, Integer pageIndex, Integer pageSize) {
        // 控制判断
        if(shopId != null && pageIndex != null && pageSize != null) {
            // 页转行
            int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
            List<ShopAuthMap> list = shopAuthMapDao.queryShopAuthMapListById(shopId, rowIndex, pageSize);
            int count = shopAuthMapDao.queryShopAuthMapCount(shopId);
            ShopAuthMapExecution shopAuthMapExecution = new ShopAuthMapExecution(ShopAuthMapStateEnum.AUTH_SUCCESS, list);
            shopAuthMapExecution.setCount(count);
            return shopAuthMapExecution;
        }else{
            return null;
        }
    }

    @Override
    public ShopAuthMap getShopAuthMapById(Long shopAuthId) {
        return shopAuthMapDao.queryShopAuthMapById(shopAuthId);
    }

    @Override
    @Transactional
    public ShopAuthMapExecution addShopAuthMap(ShopAuthMap shopAuthMap) {
        if(shopAuthMap == null || shopAuthMap.getShop() == null || shopAuthMap.getShop().getShopId() == null
          || shopAuthMap.getEmployee() == null || shopAuthMap.getEmployee().getUserId() == null){
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_INFO);
        }
        try{
            shopAuthMap.setEnableStatus(1);
            shopAuthMap.setCreateTime(new Date());
            shopAuthMap.setLastEditTime(new Date());
            int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
            if(effectNum <= 0){
                throw new ShopAuthMapOperationException("添加授权失败");
            }else{
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.AUTH_SUCCESS);
            }
        }catch(Exception e){
            logger.error("添加授权失败：" + e.getMessage());
            e.printStackTrace();
            throw new ShopAuthMapOperationException("添加授权失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ShopAuthMapExecution modifyShopAUthMap(ShopAuthMap shopAuthMap) {
        if (shopAuthMap == null || shopAuthMap.getShopAuthId() == null) {
            return new ShopAuthMapExecution(ShopAuthMapStateEnum.NULL_SHOP_AUTH_ID);
        }
        try {
            int effectNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
            if (effectNum <= 0) {
                throw new ShopAuthMapOperationException("更新授权信息失败");
            } else {
                return new ShopAuthMapExecution(ShopAuthMapStateEnum.AUTH_SUCCESS);
            }
        } catch (Exception e) {
            throw new ShopAuthMapOperationException("更新授权信息失败:" + e.getMessage());
        }
    }
}
