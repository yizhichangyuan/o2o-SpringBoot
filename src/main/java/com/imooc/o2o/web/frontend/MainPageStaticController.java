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
}
