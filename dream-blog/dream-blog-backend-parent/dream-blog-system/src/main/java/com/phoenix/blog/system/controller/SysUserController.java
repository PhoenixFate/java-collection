package com.phoenix.blog.system.controller;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.SysUser;
import com.phoenix.blog.system.request.SysUserCheckPasswordRequest;
import com.phoenix.blog.system.request.SysUserRequest;
import com.phoenix.blog.system.request.SysUserUpdatePasswordRequest;
import com.phoenix.blog.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author phoenix
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class SysUserController {

    private final ISysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    @ApiOperation("根据用户名和手机号查询用户列表接口")
    @PostMapping("/list")
    public Result search(@RequestBody SysUserRequest req) {
        return sysUserService.queryPage(req);
    }

    @ApiImplicitParam(name = "id", value = "用户Id", required = true)
    @ApiOperation("根据用户id查询所拥有的角色ids接口")
    @GetMapping("/{id}/role/ids")
    public Result findRoleIdsById(@PathVariable("id") String id) {
        return sysUserService.findRoleIdsById(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户Id", required = true),
            @ApiImplicitParam(name = "roleIds", value = "角色Id集合", required = true,
                    allowMultiple = true, dataType = "String"),
    })
    @ApiOperation("新增用户角色关系数据接口")
    @PostMapping("/{id}/role/save") // /user/9/role/save
    public Result saveUserRole(@PathVariable("id") String id,
                               @RequestBody List<String> roleIds) {
        return sysUserService.saveUserRole(id, roleIds);
    }

    @ApiImplicitParam(name = "id", value = "用户Id", required = true)
    @ApiOperation("通过用户id删除用户接口")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        return sysUserService.deleteById(id);
    }


    @ApiOperation("新增用户信息接口")
    @PostMapping // post 方式  /system/user
    public Result save(@RequestBody SysUser sysUser) {
        // 密码加密处理
        String password = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(password);
        // 新增
        sysUserService.save(sysUser);
        return Result.ok();
    }

    @ApiImplicitParam(name = "id", value = "用户ID", required = true)
    @ApiOperation("查询用户详情接口")
    @GetMapping("/{id}") // get 方式 /system/user/1
    public Result view(@PathVariable("id") String id) {
        return Result.ok(sysUserService.getById(id));
    }

    @ApiOperation("校验原密码接口")
    @PostMapping("/check/password")
    public Result checkPassword(@RequestBody SysUserCheckPasswordRequest req) {
        return sysUserService.checkPassword(req);
    }

    @ApiOperation("更新用户密码接口")
    @PutMapping("/password")
    public Result updatePassword(@RequestBody SysUserUpdatePasswordRequest req) {
        return sysUserService.updatePassword(req);
    }

    @ApiOperation("更新用户信息接口")
    @PutMapping
    public Result update(@RequestBody SysUser sysUser) {
        return sysUserService.update(sysUser);
    }

    @ApiOperation("统计总用户接口")
    @GetMapping("/total")
    public Result userTotal() {
        return sysUserService.getUserTotal();
    }


}
