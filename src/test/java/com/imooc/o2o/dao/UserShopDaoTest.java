package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserShopMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserShopDaoTest {
    @Autowired
    private UserShopMapDao userShopMapDao;

    @Test
    public void testAInsertUserShopMap(){
        UserShopMap userShopMap = new UserShopMap();
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        userShopMap.setUser(user);
        Shop shop = new Shop();
        shop.setShopId(79L);
        userShopMap.setShop(shop);
        userShopMap.setPoint(1);
        userShopMap.setCreateTime(new Date());
        int effectNum = userShopMapDao.insertUserShopMap(userShopMap);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testBUpdateUserShopMap(){
        int effectNum = userShopMapDao.updateUserShopMap(79L, 5L, 2);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testCQueryUserShopMapList(){
        UserShopMap userShopMap = new UserShopMap();
        Shop shop = new Shop();
        shop.setShopId(79L);
        userShopMap.setShop(shop);
        List<UserShopMap> list = userShopMapDao.queryUserShopMapList(userShopMap, 0, 10);
        System.out.println(list.size());
        System.out.println(list.get(0).getShop().getShopName());
    }

    @Test
    public void testDQueryUserShopMapCount(){
        UserShopMap userShopMap = new UserShopMap();
        Shop shop = new Shop();
        shop.setShopId(79L);
        userShopMap.setShop(shop);
        int count = userShopMapDao.queryUserShopMapCount(userShopMap);
        System.out.println("count:" + count);
    }

    @Test
    public void testFQueryUserShopMapById(){
        UserShopMap userShopMap = userShopMapDao.queryUserShopMapById(5L, 79L);
        System.out.println(userShopMap.getShop().getShopName());
    }
}
