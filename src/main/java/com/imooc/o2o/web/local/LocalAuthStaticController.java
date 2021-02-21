package com.imooc.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/local")
public class LocalAuthStaticController {

    @RequestMapping(value = "/accountbind")
    public String accountBind() {
        return "local/accountbind";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "local/login";
    }

    @RequestMapping(value = "/changepswpage")
    public String changePSD() {
        return "local/changepswpage";
    }
}
