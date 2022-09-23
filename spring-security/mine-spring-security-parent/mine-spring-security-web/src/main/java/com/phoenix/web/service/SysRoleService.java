package com.phoenix.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.web.entity.SysRole;

/**
 * @Author phoenix
 * @Date 9/19/22 22:12
 * @Version 1.0
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     *
     * @param page    分页参数
     * @param sysRole 条件查询对象
     * @return 分页对象
     */
    IPage<SysRole> selectPage(Page<SysRole> page, SysRole sysRole);

    /**
     * 通过角色id查询角色信息（包含权限信息）
     *
     * @param roleId 角色id
     * @return 角色
     */
    SysRole findById(Long roleId);

}
