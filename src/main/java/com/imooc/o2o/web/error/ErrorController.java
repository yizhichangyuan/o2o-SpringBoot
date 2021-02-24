package com.imooc.o2o.web.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @PackageName:com.imooc.o2o.web.error
 * @NAME:ErrorController
 * @Description:
 * @author: yizhichangyuan
 * @date:2021/2/24 19:58
 */
@Controller
@RequestMapping(value="/error")
public class ErrorController {
    @RequestMapping(value="/errorPage")
    public String errorPage(){
        return "error/errorPage";
    }
}
