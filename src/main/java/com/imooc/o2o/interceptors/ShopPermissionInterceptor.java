package com.imooc.o2o.interceptors;

import com.imooc.o2o.entity.Shop;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShopPermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
        // 如果用户操作的店铺不在自己可以执行的店铺列表里面，则返回false，此次请求不会发送到controller
        if (currentShop != null && shopList != null) {
            for (Shop temp : shopList) {
                if (temp.getShopId() == currentShop.getShopId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
