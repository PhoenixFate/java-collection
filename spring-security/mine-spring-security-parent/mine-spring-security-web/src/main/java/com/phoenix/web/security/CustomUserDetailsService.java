package com.phoenix.web.security;

import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysPermissionService;
import com.phoenix.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 查询数据库中的用户信息
 * 实现spring security提供的UserDetailsService接口
 */
@Slf4j
@Service("customUserDetailsService")
public class CustomUserDetailsService extends AbstractUserDetailsService {

    private final SysUserService sysUserService;

    public CustomUserDetailsService(SysPermissionService sysPermissionService, SysUserService sysUserService) {
        super(sysPermissionService);
        this.sysUserService = sysUserService;
    }

    @Override
    protected SysUser findSysUser(String usernameOrOther) {
        return sysUserService.findByUsername(usernameOrOther);
    }

}
