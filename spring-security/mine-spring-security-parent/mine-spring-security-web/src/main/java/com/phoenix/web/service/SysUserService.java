package com.phoenix.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phoenix.web.entity.SysUser;

/**
 * @Author phoenix
 * @Date 2022/9/19 17:48
 * @Version 1.0.0
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名 查询用户信息
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByUsername(String username);

    /**
     * 通过手机号 查询用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    SysUser findByMobile(String mobile);

    /**
     * 分页查询用户对象
     *
     * @param page 分页对象
     * @param user 查询条件
     * @return 用户列表
     */
    IPage<SysUser> selectPage(Page<SysUser> page, SysUser user);

    /**
     *
     * @param userId 用户id
     * @return 带角色的用户信息
     */
    SysUser findById(Long userId);
}
