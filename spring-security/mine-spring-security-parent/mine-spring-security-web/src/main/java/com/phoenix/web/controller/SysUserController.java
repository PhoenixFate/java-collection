package com.phoenix.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phoenix.base.result.RequestResult;
import com.phoenix.web.entity.SysRole;
import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysRoleService;
import com.phoenix.web.service.SysUserService;
import lombok.AllArgsConstructor;
import org.assertj.core.util.Lists;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
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
@RequestMapping("/user")
@AllArgsConstructor
public class SysUserController {

    private static final String HTML_PREFIX = "system/user/";

    private final SysUserService sysUserService;

    private final SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping(value = {"/", ""})
    public String user() {
        return HTML_PREFIX + "user-list";
    }

    /**
     * 跳转到新增或者修改user页面
     *
     * @return 页面
     */
    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    @GetMapping("temp/form")
    public String form() {
        return HTML_PREFIX + "user-form.html";
    }

    //返回值的code等于200，则调用成功有权限，否则返回403
    @PostAuthorize("returnObject.code==200")
    @GetMapping("/temp/delete/{id}")
    @ResponseBody
    public RequestResult deleteByIdTemp(@PathVariable Long id) {
        if (id < 0) {
            return RequestResult.build(500, "id不能小于0", id);
        }
        return RequestResult.ok();
    }

    /**
     * 通过@PreFilter过滤请求参数
     * 过滤请求参数：filterTarget 指定哪个参数，filterObject 是集合中的每个元素
     *
     * @param ids ids
     * @return RequestResult
     */
    @PreFilter(filterTarget = "ids", value = "filterObject > 0") //对参数进行过滤
    @GetMapping("/temp/delete/batch/{ids}") // /user/delete/batch/-1,0,1,2
    @ResponseBody
    public RequestResult deleteByIds(@PathVariable List<Long> ids) {
        return RequestResult.ok();
    }

    /**
     * 通过@PostFilter过滤返回结果
     * 过滤返回值：filterObject是返回值集合中的每一个元素，当表达式为true则对应元素会返回
     *
     * @return userList
     */
    @PostFilter("filterObject!=authentication.principal.username")
    @GetMapping("/temp/list")
    @ResponseBody
    public List<String> page() {
        return Lists.newArrayList("a", "b", "c", "d", "phoenix");
    }

    /**
     * 分页查询用户列表
     *
     * @param page    分页对象：size，current
     * @param sysUser 查询条件：username，mobile
     * @return 用户列表
     */
    @PreAuthorize("hasAuthority('sys:user:list')")
    @PostMapping("/page")
    @ResponseBody
    public RequestResult page(Page<SysUser> page, SysUser sysUser) {
        IPage<SysUser> sysUserIPage = sysUserService.selectPage(page, sysUser);
        return RequestResult.ok(sysUserIPage);
    }

    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    @GetMapping(value = {"/form", "/form/{id}"})
    public String form(@PathVariable(required = false) Long id, Model model) {
        SysUser sysUser = sysUserService.findById(id);
        model.addAttribute("user", sysUser);
        List<SysRole> sysRoleList = sysRoleService.list();
        model.addAttribute("roleList", sysRoleList);
        return HTML_PREFIX + "user-form";
    }

    @PreAuthorize("hasAnyAuthority('sys:user:add','sys:user:edit')")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, value = "")
    public String saveOrUpdate(SysUser sysUser) {
        //1.保存到用户表中，并且保存到用户角色关系表中
        sysUserService.saveOrUpdate(sysUser);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @DeleteMapping("{id}")
    @ResponseBody
    public RequestResult deleteById(@PathVariable Long id) {
        sysUserService.deleteById(id);
        return RequestResult.ok();
    }

}
