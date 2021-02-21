package com.imooc.o2o.web.local;

import com.imooc.o2o.dto.LocalAuthExecution;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.enums.LocalAuthStateEnum;
import com.imooc.o2o.service.LocalAuthService;
import com.imooc.o2o.util.CodeUtil;
import com.imooc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/local")
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 校验验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        // 接收参数
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        if (userName == null || password == null || personInfo == null || personInfo.getUserId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名或密码为空");
            return modelMap;
        } else {
            LocalAuth localAuth = new LocalAuth();
            localAuth.setPersonInfo(personInfo);
            localAuth.setUserName(userName);
            localAuth.setPassword(password);
            try {
                LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", localAuthExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        }
    }

    @RequestMapping(value = "/changepsd", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> changePassword(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap();
        // 当用户输入密码错误三次，verify就为true，让用户开始进行验证码校验，三次以下verify为false
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", true);
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        if (userName == null || password == null || newPassword.equals(password) || personInfo == null || personInfo.getUserId() == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名密码为空或相同密码");
            return modelMap;
        } else {
            try {
                LocalAuthExecution localAuthExecution = localAuthService.modifyPassword(userName, password, newPassword, personInfo.getUserId());
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", localAuthExecution.getStateInfo());
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        }
    }

    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 当用户输入密码错误三次，verify就为true，让用户开始进行验证码校验，三次以下verify为false
        Boolean verify = HttpServletRequestUtil.getBoolean(request, "verify");
        if (verify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", true);
            return modelMap;
        }
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        if (userName == null || password == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名密码不正确");
        } else {
            try {
                LocalAuth localAuth = localAuthService.loginByLocalAuth(userName, password);
                if (localAuth != null) {
                    modelMap.put("success", true);
                    request.getSession().setAttribute("user", localAuth.getPersonInfo());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "用户名密码不正确");
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        return modelMap;
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }
}
