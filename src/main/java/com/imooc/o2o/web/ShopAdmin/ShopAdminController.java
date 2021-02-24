package com.imooc.o2o.web.ShopAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {
    @RequestMapping(value = "/shopmanagement")
    public String shopManagement() {
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/shopoperation")
    public String shopOperation() {
        return "shop/shopOperation";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList() {
        return "shop/shoplist";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public String productCategoryManagement() {
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/productcategoryoperation")
    public String productCategoryOperation() {
        return "shop/productcategoryoperation";
    }

    @RequestMapping(value = "/productoperation")
    public String productCategory() {
        return "shop/productoperation";
    }

    @RequestMapping(value = "/productlist")
    public String productlist() {
        return "shop/productlist";
    }

    @RequestMapping(value="/awardlistmanagement")
    public String awardList(){
        return "shop/awardlistmanagement";
    }

    @RequestMapping(value="/productbuycheck")
    public String productBuyCheck(){
        return "shop/productbuycheck";
    }


    @RequestMapping(value="/shopmemberlist")
    public String shopMemberList(){
        return "shop/shopmemberlist";
    }

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

    @RequestMapping(value="/awardedit")
    public String awardOperation(){
        return "shop/awardedit";
    }

    @RequestMapping(value="/userawardlist")
    public String userAwardList(){
        return "shop/userawardlist";
    }
}
