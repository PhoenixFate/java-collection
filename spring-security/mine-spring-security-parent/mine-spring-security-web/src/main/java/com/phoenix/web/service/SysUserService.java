package com.phoenix.web.service;

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

}
