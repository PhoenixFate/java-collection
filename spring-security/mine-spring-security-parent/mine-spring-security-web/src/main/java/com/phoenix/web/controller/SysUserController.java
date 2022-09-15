package com.phoenix.web.controller;

import com.phoenix.base.result.RequestResult;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "/user-list";
    }

    /**
     * 跳转到新增或者修改user页面
     *
     * @return 页面
     */
    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    @GetMapping("/form")
    public String form() {
        return HTML_PREFIX + "/user-form.html";
    }

    //返回值的code等于200，则调用成功有权限，否则返回403
    @PostAuthorize("returnObject.code==200")
    @GetMapping("/delete/{id}")
    public RequestResult deleteById(@PathVariable Long id) {
        return RequestResult.ok();
    }

}
