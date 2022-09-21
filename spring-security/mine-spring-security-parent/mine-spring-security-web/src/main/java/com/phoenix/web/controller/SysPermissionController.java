package com.phoenix.web.controller;

import com.phoenix.base.result.RequestResult;
import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.service.SysPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户管理 web层
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/14 17:27
 */
@Controller
@RequestMapping("/permission")
@AllArgsConstructor
public class SysPermissionController {

    private static final String HTML_PREFIX = "system/permission";

    private final SysPermissionService sysPermissionService;

    @PreAuthorize("hasAuthority('sys:permission')")
    @GetMapping(value = {"/", ""})
    public String permission() {
        return HTML_PREFIX + "/permission-list";
    }

    @PreAuthorize("hasAuthority('sys:permission')")
    @PostMapping("/list")
    @ResponseBody
    public RequestResult list() {
        List<SysPermission> sysPermissionList = sysPermissionService.list();
        return RequestResult.ok(sysPermissionList);
    }
}
