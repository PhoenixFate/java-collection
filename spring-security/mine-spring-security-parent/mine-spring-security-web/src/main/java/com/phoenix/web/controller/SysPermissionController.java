package com.phoenix.web.controller;

import com.phoenix.base.result.RequestResult;
import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.service.SysPermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理 web层
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/14 17:27
 */
@Controller
@Slf4j
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

    /**
     * 跳转到新增或者修改页面
     * /form 新增页面
     * /form/{id} 修改页面
     *
     * @return 路径变量
     */
    @PreAuthorize("hasAnyAuthority('sys:permission:edit','sys:permission:add')")
    @GetMapping(value = {"/form", "/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        //Model model就等价于Map<String,Object>
        log.info("------------- /permission/form/id: {}", id);
        SysPermission sysPermission = null;
        if (id != null) {
            //1.通过id查询对应权限信息
            sysPermission = sysPermissionService.getById(id);
            //也可以通过前端来匹配parentName
            SysPermission parent = sysPermissionService.getById(sysPermission.getParentId());
            sysPermission.setParentName(parent.getName());
        } else {
            sysPermission = new SysPermission();
        }
        //绑定后页面可获取
        model.addAttribute("permission", sysPermission);
        return HTML_PREFIX + "/permission-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:permission:edit','sys:permission:add')")
    @RequestMapping(value = "", method = {RequestMethod.PUT, RequestMethod.POST})
    public String saveOrUpdate(SysPermission sysPermission) {
        sysPermissionService.saveOrUpdate(sysPermission);
        return "redirect:/permission";
    }

    @PreAuthorize("hasAuthority('sys:permission:delete')")
    @DeleteMapping("/{id}")
    @ResponseBody
    public RequestResult deleteById(@PathVariable Long id) {
        sysPermissionService.deleteById(id);
        return RequestResult.ok();
    }
}
