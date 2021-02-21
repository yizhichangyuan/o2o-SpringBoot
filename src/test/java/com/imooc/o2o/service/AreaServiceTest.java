package com.imooc.o2o.service;

import com.imooc.o2o.entity.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest{
    @Autowired
    private AreaService areaService;
    @Autowired
    private CacheService cacheService;

    @Test
    public void getAreaList() {
        List<Area> areaList = areaService.queryArea();
        System.out.println(areaList.get(0).getAreaName());
        cacheService.removeKeys(areaService.AREALISTKEY);
        List<Area> temp = areaService.queryArea();
        assertEquals("西苑", areaList.get(0).getAreaName());
    }
}
