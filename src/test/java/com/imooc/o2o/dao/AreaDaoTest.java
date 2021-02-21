package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class) // springboot的测试框架
@SpringBootTest // 引入编码文件，才可以使用autowired
public class AreaDaoTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea() {
        List<Area> list = areaDao.queryArea();
        assertEquals(3, list.size());
    }
}
