package com.imooc.o2o.service.impl;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ShopAuthMapExecution;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import com.imooc.o2o.enums.ShopAuthMapStateEnum;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ShopOperationException;
import com.imooc.o2o.service.ShopAuthMapService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PathUtil;
import com.imooc.o2o.util.RowIndexCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, ImageHolder thumbnail) {
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            // 给店铺信息赋予初始值
            shop.setEnableStatus(ShopStateEnum.CHECK.getState());
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            shop.setAdvice("审核中");
            shop.setPriority(1);
            // 添加店铺信息
            int effectedNum = shopDao.insertShop(shop); //mybatis插入主键后，会自动给shop添加主键
            if (effectedNum <= 0) {
                // 这里如果是Exception，则事务发生错误还是会提交
                throw new ShopOperationException("店铺创建失败");
            } else {
                if (thumbnail.getImage() != null) {
                    // 存储图片，传入的shop是引用类型，里面会给shop赋予图片的地址
                    try {
                        addShopImg(shop, thumbnail);
                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }
                    // 更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
            // 执行增加shopAuthMap操作，将当前操作者设置为店家
            ShopAuthMap shopAuthMap = new ShopAuthMap();
            shopAuthMap.setShop(shop);
            shopAuthMap.setEmployee(shop.getOwner());
            shopAuthMap.setTitleFlag(0);
            shopAuthMap.setTitle("店家");
            ShopAuthMapExecution execution = shopAuthMapService.addShopAuthMap(shopAuthMap);
            if(execution.getState() != ShopAuthMapStateEnum.AUTH_SUCCESS.getState()){
                throw new ShopOperationException("授权店家失败");
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    @Override
    public Shop getShopByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    @Transactional
    public ShopExecution modifyShop(Shop shop, ImageHolder thumbnail) {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //1.更新店铺图片，如果确实有新的图片上传，则删除该店铺旧的图片，并添加水印更新shop.addr
            if (thumbnail.getImage() != null && thumbnail.getImageName() != null && !"".equals(thumbnail.getImageName())) {
                Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                try {
                    addShopImg(shop, thumbnail);
                } catch (IOException e) {
                    throw new IOException("存储图片失败" + e.getMessage());
                } catch (Exception e) {
                    throw new ShopOperationException("addShopImg error:" + e.getMessage());
                }
            }
            //2.更新店铺的信息
            shop.setLastEditTime(new Date());
            int effectNum = shopDao.updateShop(shop);
            if (effectNum <= 0) {
                return new ShopExecution(ShopStateEnum.INNER_ERROR);
            } else {
                shop = shopDao.queryByShopId(shop.getShopId());
                return new ShopExecution(ShopStateEnum.SUCCESS, shop);
            }
        } catch (Exception e) {
            throw new ShopOperationException("modifyShop error:" + e.getMessage());
        }
    }

    @Override
    public int getShopCount(Shop shopCondition) {
        try {
            int count = shopDao.queryShopCount(shopCondition);
            return count;
        } catch (Exception e) {
            throw new ShopOperationException("getShopCount error:" + e.getMessage());
        }
    }

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        int rowIndex = RowIndexCalculator.calRowIndex(pageIndex, pageSize);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        int shopCount = shopDao.queryShopCount(shopCondition); //查询符合同条件的店铺总数，便于计算分页总数
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setState(ShopStateEnum.SUCCESS.getState());
            se.setStateInfo(ShopStateEnum.SUCCESS.getStateInfo());
            se.setShopList(shopList);
            se.setCount(shopCount);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
            se.setStateInfo(ShopStateEnum.INNER_ERROR.getStateInfo());
        }
        return se;
    }


    private void addShopImg(Shop shop, ImageHolder imageHolder) throws IOException {
        // 获取shop图片目录的相对值的路径
        String targetAddr = PathUtil.getShopImgPath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(imageHolder, targetAddr);
        shop.setShopImg(shopImgAddr);
    }
}
