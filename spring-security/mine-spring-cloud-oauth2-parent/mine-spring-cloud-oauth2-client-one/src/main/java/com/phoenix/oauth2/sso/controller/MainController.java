package com.phoenix.oauth2.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author phoenix
 * @Date 10/7/22 14:46
 * @Version 1.0
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/member")
    public String member() {
        return "member";
    }
}
