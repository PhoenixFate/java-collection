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
@RequestMapping("/role")
public class SysRoleController {

    private static final String HTML_PREFIX = "system/role";

    @GetMapping(value = {"/", ""})
    public String role() {
        return HTML_PREFIX + "/role-list";
    }

}
