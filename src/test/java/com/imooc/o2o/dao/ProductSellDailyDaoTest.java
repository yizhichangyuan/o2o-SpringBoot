package com.imooc.o2o.dao;

import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductSellDailyDaoTest {
    @Autowired
    private ProductSellDailyDao productSellDailyDao;

    @Test
    public void testAInsertProductSellDaily(){
        int effectNum = productSellDailyDao.insertProductSellDaily();
        System.out.println(effectNum);
    }

    @Test
    public void testBQueryProductSellDailyList() throws ParseException {
        ProductSellDaily productSellDaily = new ProductSellDaily();
        Shop shop = new Shop();
        shop.setShopId(79L);
        productSellDaily.setShop(shop);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginTime = format.parse("2021-02-17");
        Date endTime = format.parse("2021-02-19");
        List<ProductSellDaily> list = productSellDailyDao.queryProductSellDailyList(productSellDaily, beginTime, endTime);
        System.out.println(list.size());
    }
}
