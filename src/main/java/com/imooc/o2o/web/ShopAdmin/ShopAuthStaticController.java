package com.imooc.o2o.web.ShopAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @PackageName:com.imooc.o2o.web.ShopAdmin
 * @NAME:ShopAuthStaticController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/20 10:33
 */
@Controller
@RequestMapping(value="shopadmin")
public class ShopAuthStaticController {
    @RequestMapping(value="/shopauthmanagement")
    public String listshopauth(){
        return "shop/shopauthmanagement";
    }

    @RequestMapping(value="/shopauthedit")
    public String shopAuthEdit(){
        return "shop/shopauthedit";
    }

    @RequestMapping(value="/shopauthsuccess")
    public String shopAuthSuccess(){
        return "shop/shopauthsuccess";
    }

    @RequestMapping(value="/shopauthfail")
    public String shopAuthFail(){
        return "shop/shopauthfail";
    }
}
