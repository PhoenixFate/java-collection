package com.phoenix.blog.system.controller;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.SysMenu;
import com.phoenix.blog.system.request.SysMenuRequest;
import com.phoenix.blog.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author phoenix
 */
@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/menu")
@AllArgsConstructor
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    /**
     * 根据菜单名称查询列表接口
     *
     * @param sysMenuRequest 菜单查询条件
     * @return 菜单列表
     */
    @ApiOperation("根据菜单名称查询列表接口")
    @PostMapping("/list") // localhost:8003/system/menu/search
    @ApiImplicitParam(name = "sysMenuRequest", value = "带搜索条件的菜单查询对象", dataType = "SysMenuRequest", required = true)
    public Result search(@RequestBody SysMenuRequest sysMenuRequest) {
        return sysMenuService.queryList(sysMenuRequest);
    }

    /**
     * 通过菜单ID删除菜单及子菜单接口
     *
     * @param id 菜单id
     * @return 是否删除成功
     */
    @ApiOperation("通过菜单ID删除菜单及子菜单接口")
    @ApiImplicitParam(name = "id", value = "菜单id", required = true, type = "string")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") String id) {
        return sysMenuService.deleteById(id);
    }

    /**
     * 新增菜单信息接口
     *
     * @param sysMenu 菜单信息
     * @return 是否新增成功
     */
    @ApiOperation("新增菜单信息接口")
    @PostMapping
    @ApiImplicitParam(name = "sysMenu", value = "菜单信息", dataType = "SysMenu", required = true)
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    /**
     * 查询菜单详情接口
     *
     * @param id 菜单id
     * @return 菜单详情
     */
    @ApiOperation("查询菜单详情接口")
    @ApiImplicitParam(name = "id", value = "菜单ID", required = true)
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") String id) {
        return Result.ok(sysMenuService.getById(id));
    }

    /**
     * 更新菜单信息接口
     *
     * @param sysMenu 菜单信息
     * @return 是否更新成功
     */
    @ApiOperation("更新菜单信息接口")
    @PutMapping
    @ApiImplicitParam(name = "SysMenu", value = "菜单信息", dataType = "SysMenu", required = true)
    public Result update(@RequestBody SysMenu sysMenu) {
        sysMenu.setUpdateDate(new Date());
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    /**
     * 通过用户id查询所拥有的权限菜单树和按钮
     *
     * @param userId 用户id
     * @return 某用户的所有菜单树
     */
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, type = "String")
    @ApiOperation("通过用户id查询所拥有的权限菜单树和按钮")
    @GetMapping("/user/{userId}") // get方式 /menu/user/{userId}
    public Result findUserMenuTreeAndButton(@PathVariable("userId") String userId) {
        return sysMenuService.findUserMenuTree(userId);
    }

    @ApiImplicitParam(name = "userId", value = "用户id", required = true, type = "String")
    @ApiOperation("通过用户id查询权限列表")
    @GetMapping("/list/user/{userId}")
    public List<SysMenu> findMenuListByUserId(@PathVariable("userId") String userId) {
        return sysMenuService.findByUserId(userId);
    }


}
