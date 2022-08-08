package com.phoenix.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping({"/index","/",""})
    public String index(){
        //返回resources/templates/index.html
        return "index";
    }
}
