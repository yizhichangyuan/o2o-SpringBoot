package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopAuthMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @PackageName:com.imooc.o2o.dao
 * @NAME:ShopAuthDaoTest
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/19 20:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopAuthDaoTest {
    @Autowired
    private ShopAuthMapDao shopAuthMapDao;

    @Test
    public void testAInsertShopAuthMap(){
//        ShopAuthMap shopAuthMap = new ShopAuthMap();
//        PersonInfo employee = new PersonInfo();
//        employee.setUserId(5L);
//        shopAuthMap.setEmployee(employee);
//        Shop shop = new Shop();
//        shop.setShopId(79L);
//        shopAuthMap.setShop(shop);
//        shopAuthMap.setTitle("老板");
//        shopAuthMap.setTitleFlag(1);
//        shopAuthMap.setCreateTime(new Date());
//        shopAuthMap.setLastEditTime(new Date());
//        shopAuthMap.setEnableStatus(1);
//        int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap);
//        assertEquals(effectNum, 1);

        ShopAuthMap shopAuthMap2 = new ShopAuthMap();
        PersonInfo employee2 = new PersonInfo();
        employee2.setUserId(4L);
        shopAuthMap2.setEmployee(employee2);
        Shop shop2 = new Shop();
        shop2.setShopId(79L);
        shopAuthMap2.setShop(shop2);
        shopAuthMap2.setTitle("前台");
        shopAuthMap2.setTitleFlag(0);
        shopAuthMap2.setCreateTime(new Date());
        shopAuthMap2.setLastEditTime(new Date());
        shopAuthMap2.setEnableStatus(1);
        int effectNum = shopAuthMapDao.insertShopAuthMap(shopAuthMap2);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testBQueryShopAuthMapListById(){
        List<ShopAuthMap> list = shopAuthMapDao.queryShopAuthMapListById(79L, 0, 10);
        assertEquals(list.size(), 2);
        System.out.println(list.get(0).getEmployee().getName());
    }

    @Test
    public void testCQueryShopAuthListCount(){
        int count = shopAuthMapDao.queryShopAuthMapCount(79L);
        System.out.println("count:" + count);
    }

    @Test
    public void testUpdateShopAuthMap(){
        ShopAuthMap shopAuthMap = new ShopAuthMap();
        shopAuthMap.setLastEditTime(new Date());
        shopAuthMap.setTitle("员工");
        shopAuthMap.setShopAuthId(2);
        int effectNum = shopAuthMapDao.updateShopAuthMap(shopAuthMap);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testEQueryShopAuthMapById(){
        ShopAuthMap shopAuthMap = shopAuthMapDao.queryShopAuthMapById(1);
        System.out.println(shopAuthMap.getEmployee().getName());
    }

    @Test
    public void testFDeleteShopAuthMap(){
        int effectNum = shopAuthMapDao.deleteShopAuthMap(79L, 4);
        assertEquals(effectNum, 1);
    }
}
