package com.imooc.o2o.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @PackageName:com.imooc.o2o.interceptors
 * @NAME:ErrorPageInterceptor 发生指定错误状态码拦截器
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/24 19:22
 */
public class ErrorPageInterceptor implements HandlerInterceptor {
    private List<Integer> errorCodeList = Arrays.asList(404, 403, 500, 501);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(errorCodeList.contains(response.getStatus())){
            // 直接跳转定制错误页面
            response.sendRedirect("/o2o/error/errorPage");
            return false;
        }
        return true;
    }
}
