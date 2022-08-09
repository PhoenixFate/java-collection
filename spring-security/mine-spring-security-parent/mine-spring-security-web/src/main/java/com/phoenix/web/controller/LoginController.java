package com.phoenix.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login/page")
    public String toLogin(){
        //会自动寻找classpath下面 /template/login.html页面
        return "/login";
    }


}
