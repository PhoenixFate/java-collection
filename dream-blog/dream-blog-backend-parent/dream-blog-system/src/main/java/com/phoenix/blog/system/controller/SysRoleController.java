package com.phoenix.blog.system.controller;

import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.entity.SysRole;
import com.phoenix.blog.system.request.SysRoleRequest;
import com.phoenix.blog.system.service.ISysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author phoenix
 */
@Api(value = "角色管理接口")
@RestController
@RequestMapping("/role")
@AllArgsConstructor
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    /**
     * 根据角色名称查询角色列表接口
     *
     * @param sysRoleRequest 角色条件查询对象
     * @return 角色列表
     */
    @ApiOperation("根据角色名称查询角色列表接口")
    @PostMapping("/search")
    @ApiImplicitParam(name = "sysRoleRequest", value = "带搜索条件的角色查询对象", dataType = "SysRoleRequest", required = true)
    public Result search(@RequestBody SysRoleRequest sysRoleRequest) {
        return sysRoleService.queryPage(sysRoleRequest);
    }

    /**
     * 新增角色信息接口
     *
     * @param sysRole 系统角色信息
     * @return 是否新增成功
     */
    @ApiOperation("新增角色信息接口")
    @PostMapping
    @ApiImplicitParam(name = "sysRole", value = "角色对象", dataType = "SysRole", required = true)
    public Result save(@RequestBody SysRole sysRole) {
        sysRoleService.save(sysRole);
        return Result.ok();
    }

    /**
     * 查询角色详情接口
     *
     * @param id 角色id
     * @return 角色详情
     */
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, type = "String")
    @ApiOperation("查询角色详情接口")
    @GetMapping("/{id}")
    public Result view(@PathVariable("id") String id) {
        return Result.ok(sysRoleService.getById(id));
    }

    /**
     * 更新角色信息接口
     *
     * @param sysRole 角色
     * @return 是否更新成功
     */
    @ApiOperation("更新角色信息接口")
    @PutMapping
    @ApiImplicitParam(name = "sysRole", value = "角色对象", dataType = "SysRole", required = true)
    public Result update(@RequestBody SysRole sysRole) {
        sysRole.setUpdateDate(new Date());
        sysRoleService.updateById(sysRole);
        return Result.ok();
    }

    /**
     * 删除角色信息及角色菜单关系接口
     *
     * @param id 角色id
     * @return 是否删除成功
     */
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, type = "String")
    @ApiOperation("删除角色信息及角色菜单关系接口")
    @DeleteMapping("/{id}") // /system/role/1
    public Result delete(@PathVariable("id") String id) {
        return sysRoleService.deleteById(id);
    }

    /**
     * 根据角色id查询拥有的菜单ids接口
     *
     * @param id 角色id
     * @return 某角色下的所有菜单id集合
     */
    @ApiImplicitParam(name = "id", value = "角色ID", required = true, type = "String")
    @ApiOperation("根据角色id查询拥有的菜单ids接口")
    @GetMapping("/{id}/menu/ids") // /system/role/1/menu/ids
    public Result findMenuIdsById(@PathVariable("id") String id) {
        return sysRoleService.findMenuIdsById(id);
    }

    /**
     * 新增角色菜单关系数据接口
     *
     * @param id      角色id
     * @param menuIds 菜单id集合
     * @return 是否新增成功
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色ID", required = true),
            @ApiImplicitParam(allowMultiple = true, dataType = "String",
                    name = "menuIds", value = "菜单id集合", required = true)
    })
    @ApiOperation("新增角色菜单关系数据接口")
    @PostMapping("/{id}/menu/save")
    public Result saveRoleMenu(@PathVariable("id") String id,
                               @RequestBody List<String> menuIds) {
        return sysRoleService.saveRoleMenu(id, menuIds);
    }

}
