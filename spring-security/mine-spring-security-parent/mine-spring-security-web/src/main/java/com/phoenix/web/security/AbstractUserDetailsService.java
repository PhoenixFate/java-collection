package com.phoenix.web.security;

import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author phoenix
 * @Date 9/20/22 23:49
 * @Version 1.0
 */
@AllArgsConstructor
public abstract class AbstractUserDetailsService implements UserDetailsService {

    private SysPermissionService sysPermissionService;

    /**
     * 该用法交给子类去实现，用于查询用户信息
     *
     * @param usernameOrOther 用户名或者其他例如手机号邮箱等，用户查询用户等唯一标识
     * @return 用户信息
     */
    protected abstract SysUser findSysUser(String usernameOrOther);

    @Override
    public UserDetails loadUserByUsername(String usernameOrOther) throws UsernameNotFoundException {
        //1.通过用户名或者手机或者其他例如邮箱等查询用户信息
        SysUser sysUser = findSysUser(usernameOrOther);
        //2.通过用户id获取权限信息
        findSysPermission(sysUser);
        return sysUser;
    }

    private void findSysPermission(SysUser sysUser) {
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或者密码错误或者未注册");
        }
        //构建UserDetails
        //2.查询该用户的所有权限
        List<SysPermission> sysPermissionList = sysPermissionService.findPermissionByUserId(sysUser.getId());
        //在左侧菜单动态渲染的时候会使用
        sysUser.setPermissions(sysPermissionList);
        //3.封装用户信息和权限信息
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (SysPermission sysPermission : sysPermissionList) {
            String code = sysPermission.getCode();
            grantedAuthorityList.add(new SimpleGrantedAuthority(code));
        }
        sysUser.setAuthorities(grantedAuthorityList);
    }
}
