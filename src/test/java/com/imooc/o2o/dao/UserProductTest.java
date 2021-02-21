package com.imooc.o2o.dao;

import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserProductMap;
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
public class UserProductTest {
    @Autowired
    private UserProductMapDao userProductMapDao;

    @Test
    public void testAInsertUserProduct(){
        UserProductMap userProductMap = new UserProductMap();
        userProductMap.setCreateTime(new Date());
        userProductMap.setPoint(1);
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        userProductMap.setUser(user);
        PersonInfo operator = new PersonInfo();
        operator.setUserId(5L);
        userProductMap.setOperator(operator);
        Shop shop = new Shop();
        shop.setShopId(79L);
        userProductMap.setShop(shop);
        Product product = new Product();
        product.setProductId(30L);
        userProductMap.setProduct(product);
        int effectNum = userProductMapDao.insertUserProduct(userProductMap);
        assertEquals(effectNum, 1);

        UserProductMap userProductMap2 = new UserProductMap();
        userProductMap2.setCreateTime(new Date());
        userProductMap2.setPoint(2);
        PersonInfo user2 = new PersonInfo();
        user2.setUserId(5L);
        userProductMap2.setUser(user2);
        PersonInfo operator2 = new PersonInfo();
        operator2.setUserId(5L);
        userProductMap2.setOperator(operator2);
        Shop shop2 = new Shop();
        shop2.setShopId(79L);
        userProductMap2.setShop(shop2);
        Product product2 = new Product();
        product2.setProductId(30L);
        userProductMap2.setProduct(product2);
        effectNum = userProductMapDao.insertUserProduct(userProductMap2);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testBQueryUserProductList(){
        UserProductMap userProductMap = new UserProductMap();
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        userProductMap.setUser(user);
        List<UserProductMap> list = userProductMapDao.queryUserProductList(userProductMap, 0, 10);
        System.out.println(list.size());

        UserProductMap userProductMap2 = new UserProductMap();
        Shop shop = new Shop();
        shop.setShopId(79L);
        userProductMap2.setShop(shop);
        List<UserProductMap> temp = userProductMapDao.queryUserProductList(userProductMap2, 0, 10);
        System.out.println(temp.get(0).getShop().getShopName());
    }

    @Test
    public void testCQueryUserProductCount(){
        UserProductMap userProductMap = new UserProductMap();
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        userProductMap.setUser(user);
        int count = userProductMapDao.queryUserProductCount(userProductMap);
        System.out.println("count:" + count);
    }
}
