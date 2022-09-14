package com.phoenix.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户管理 web层
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/14 17:27
 */
@Controller
@RequestMapping("/user")
public class SysUserController {

    private static final String HTML_PREFIX = "system/user";

    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "/user-list";
    }

}
