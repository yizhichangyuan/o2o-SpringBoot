package com.imooc.o2o.web.ShopAdmin;

import com.imooc.o2o.dto.ProductSellDailyExecution;
import com.imooc.o2o.dto.Series;
import com.imooc.o2o.dto.XSeries;
import com.imooc.o2o.entity.ProductSellDaily;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductSellDailyStateEnum;
import com.imooc.o2o.service.ProductSellDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:ProductSellDailyController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/21 20:06
 */
@RestController
@RequestMapping(value="/shopadmin")
public class ProductSellDailyController {
    @Autowired
    private ProductSellDailyService productSellDailyService;

    @RequestMapping(value="/productselldaily")
    private Map<Object, Object> getProductSellDaily(HttpServletRequest request){
        Map<Object, Object> modelMap = new HashMap<>();
        Set<String> productNameList = new LinkedHashSet<String>(); // 保证元素添加顺序且不重复
        List<Series> seriesList = new ArrayList<>(); // echarts中series列表
        XSeries xSeries = new XSeries(); // 横坐标列表

        Shop currentShop = (Shop)request.getSession().getAttribute("currentShop");
        if(currentShop != null && currentShop.getShopId() != null){
            Calendar calendar = Calendar.getInstance();
            // 获取昨日的时间
            calendar.add(Calendar.DATE, -1);
            Date yesterday = calendar.getTime();
            // 在昨日的时间基础上获取前六天的时间
            calendar.add(Calendar.DATE, -6);
            Date sevenDayAgo = calendar.getTime();
            // 查询该店铺的过去七天的日销量
            ProductSellDaily productSellDaily = new ProductSellDaily();
            productSellDaily.setShop(currentShop);
            ProductSellDailyExecution psdExecution= productSellDailyService.queryProductSellDailyList(productSellDaily, yesterday, sevenDayAgo);
            List<Integer> productSellData = new ArrayList<>();
            if(psdExecution.getState() == ProductSellDailyStateEnum.SUCCESS.getState()){
                List<ProductSellDaily> list = psdExecution.getList();
                String tempProductName = ""; // 辅助划分
                // 将产品销量按照产品名称进行划分
                for(int i = 0; i < list.size(); i++){
                    ProductSellDaily currentPSD = list.get(i);
                    if(!currentPSD.getProduct().getProductName().equals((tempProductName)) && !tempProductName.isEmpty()){
                        // 将之前的属于同一个名称的product销量封装到Series中
                        Series series = new Series();
                        series.setData(productSellData);
                        series.setName(tempProductName);
                        seriesList.add(series);
                        tempProductName = currentPSD.getProduct().getProductName();
                        productSellData = new ArrayList<>();
                        productSellData.add(currentPSD.getTotal());
                    }else{
                        tempProductName = currentPSD.getProduct().getProductName();
                        productSellData.add(currentPSD.getTotal());
                    }
                    productNameList.add(tempProductName);
                    xSeries.getData().add(new SimpleDateFormat("yyyy-MM-dd").format(currentPSD.getDays()));
                    // 对于最后一个特殊处理一下
                    if(i == list.size() - 1){
                        Series series = new Series();
                        series.setData(productSellData);
                        series.setName(tempProductName);
                        seriesList.add(series);
                    }
                }
                modelMap.put("success", true);
                modelMap.put("series", seriesList);
                modelMap.put("legend", productNameList);
                modelMap.put("xData", xSeries);
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", psdExecution.getStateInfo());
            }
        }
        return modelMap;
    }
}
