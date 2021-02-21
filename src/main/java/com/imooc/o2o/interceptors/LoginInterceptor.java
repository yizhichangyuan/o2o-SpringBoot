package com.imooc.o2o.interceptors;

import com.imooc.o2o.entity.PersonInfo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        if (personInfo != null && personInfo.getUserId() != null && personInfo.getEnableStatus() != 0) {
            // 返回true表示请求接着向下传递到下一个拦截器
            return true;
        }
        // 如果没有，说明用户进入该页面不是通过登录页面进入，将用户导向到登陆页面
        PrintWriter printWriter = response.getWriter();
        printWriter.println("<html>");
        printWriter.println("<script>");
        printWriter.println("window.open('" + request.getContextPath() + "/local/login?userType=2','_self')");
        printWriter.println("</script>");
        printWriter.println("</html>");
        return false;
    }
}
