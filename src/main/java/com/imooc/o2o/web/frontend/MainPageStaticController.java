package com.imooc.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/frontend")
public class MainPageStaticController {

    @RequestMapping(value = "/index")
    private String getFrontendIndex() {
        return "frontend/index";
    }

    @RequestMapping(value = "/listshopspage")
    private String listShops() {
        return "frontend/listshops";
    }

    @RequestMapping("/shopdetailpage")
    private String shopDetailPage() {
        return "frontend/shopdetailpage";
    }

    @RequestMapping("/productdetail")
    private String productDetail() {
        return "frontend/productdetail";
    }

    @RequestMapping(value="/shopawardexchange")
    public String shopAwardExchange(){
        return "frontend/shopawardexchange";
    }

    @RequestMapping(value="/userawardpage")
    public String userAwardPage(){
        return "frontend/userawardpage";
    }

    @RequestMapping(value="/awardexchangestatus")
    public String awardExchangeStatus(){
        return "frontend/awardexchangestatus";
    }

    @RequestMapping(value="/userproductpage")
    public String userProductPage(){
        return "frontend/userproductpage";
    }
}
