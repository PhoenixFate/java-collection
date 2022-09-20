package com.phoenix.web.security;

import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysPermissionService;
import com.phoenix.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通过手机号获取用户信息和权限列表
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/17 15:00
 */
@Service
@Slf4j
public class MobileUserDetailsService extends AbstractUserDetailsService {

    private final SysUserService sysUserService;

    public MobileUserDetailsService(SysUserService sysUserService, SysPermissionService sysPermissionService) {
        super(sysPermissionService);
        this.sysUserService = sysUserService;
    }

    @Override
    protected SysUser findSysUser(String mobile) {
        log.info("当前登录的手机号为: {}", mobile);
        return sysUserService.findByMobile(mobile);
    }

}
