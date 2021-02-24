package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Award;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.UserAwardMap;
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
public class UserAwardDaoTest {
    @Autowired
    private UserAwardMapDao userAwardMapDao;

    @Test
    public void testAInsertUserAward(){
        UserAwardMap userAward = new UserAwardMap();
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        PersonInfo operator = new PersonInfo();
        operator.setUserId(5L);
        Award award = new Award();
        award.setAwardId(2L);
        Shop shop = new Shop();
        shop.setShopId(79L);
        userAward.setAward(award);
        userAward.setUser(user);
        userAward.setShop(shop);
        userAward.setOperator(operator);
        userAward.setPoint(2);
        userAward.setCreateTime(new Date());
        userAward.setLastEditTime(new Date());
        userAward.setUsedStatus(1);
        int effectNum = userAwardMapDao.insertUserAward(userAward);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testBUpdateUserAward(){
        userAwardMapDao.updateUserAward(5L, 1L, new Date(), 0, 5L);
    }

    @Test
    public void testCQueryUserAwardById(){
        UserAwardMap userAwardMap = userAwardMapDao.queryUserAwardById(1L);
        System.out.println(userAwardMap.getOperator().getName());
        System.out.println(userAwardMap.getUser().getName());
        System.out.println(userAwardMap.getAward().getAwardName());
        System.out.println(userAwardMap.getShop().getShopId());
    }

    @Test
    public void testDQueryUserAwardList(){
        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        userAwardMap.setUser(user);
        List<UserAwardMap> list = userAwardMapDao.queryUserAwardList(userAwardMap,0, 10);
        System.out.println(list.size());

        UserAwardMap temp = new UserAwardMap();
        PersonInfo userTemp = new PersonInfo();
        userTemp.setName("sik");
        temp.setOperator(userTemp);
        List<UserAwardMap> tempList = userAwardMapDao.queryUserAwardList(userAwardMap,0, 10);
        System.out.println(tempList.size());

        UserAwardMap tempO = new UserAwardMap();
        Award award = new Award();
        award.setAwardName("test2");
        tempO.setAward(award);
        List<UserAwardMap> tempListO = userAwardMapDao.queryUserAwardList(userAwardMap,0, 10);
        System.out.println(tempListO.size());
    }

    @Test
    public void testFQueryCount(){
        UserAwardMap userAwardMap = new UserAwardMap();
        PersonInfo user = new PersonInfo();
        user.setUserId(5L);
        userAwardMap.setUser(user);
        int count = userAwardMapDao.queryUserAwardCount(userAwardMap);
        System.out.println("count:" + count);
    }
}
