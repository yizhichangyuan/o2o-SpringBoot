package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Award;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest {
    @Autowired
    private AwardDao awardDao;

    @Test
    public void testAInsertAward(){
        Award award = new Award();
        award.setAwardName("test2");
        award.setAwardDesc("test");
        award.setAwardImg("test");
        award.setPoint(1);
        award.setShopId(83);
        award.setPriority(1);
        award.setCreateTime(new Date());
        award.setLastEditTime(new Date());
        award.setEnableStatus(1);
        int effectNum = awardDao.insertAward(award);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testBUpdateAward(){
        Award award = new Award();
        award.setAwardName("修改名称");
        award.setAwardId(1);
        int effectNum = awardDao.updateAward(award);
        assertEquals(effectNum, 1);
    }

    @Test
    public void queryCQueryAward(){
        Award award = awardDao.queryAwardById(1L);
        System.out.println(award.getAwardDesc());
    }

    @Test
    public void queryDQueryAwardList(){
        Award award = new Award();
        award.setAwardName("test");
        List<Award> list = awardDao.queryAwardList(award, 0, 10);
        System.out.println(list.size());
    }

    @Test
    public void testFDeleteAward(){
        int effectNum = awardDao.deleteAward(1L, 83L);
        assertEquals(effectNum, 1);
    }

    @Test
    public void testEQueryCount(){
        Award award = new Award();
        award.setAwardName("test");
        int count = awardDao.queryAwardCount(award);
        System.out.println(count);
    }


}
