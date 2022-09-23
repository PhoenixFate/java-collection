package com.phoenix.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phoenix.base.result.RequestResult;
import com.phoenix.web.entity.SysRole;
import com.phoenix.web.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private SysRoleService sysRoleService;

    private static final String HTML_PREFIX = "system/role/";

    @GetMapping(value = {"/", ""})
    public String role() {
        return HTML_PREFIX + "role-list";
    }

    @PreAuthorize("hasAuthority('sys:role:list')")
    @PostMapping("/page") //role/page
    @ResponseBody
    public RequestResult page(Page<SysRole> page, SysRole sysRole) {
        IPage<SysRole> sysRoleIPage = sysRoleService.selectPage(page, sysRole);
        return RequestResult.ok(sysRoleIPage);
    }

    /**
     * 跳转新增或者修改页面
     *
     * @param id    roleId
     * @param model 前端model
     * @return 带权限的角色信息
     */
    @PreAuthorize("hasAnyAuthority('sys:role:add','sys:role:edit')")
    @GetMapping(value = {"/form", "/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            //通过角色id查询对应的角色信息和权限信息
            SysRole sysRole = sysRoleService.findById(id);
            model.addAttribute("role", sysRole);
        }
        return HTML_PREFIX + "role-form";
    }

}
