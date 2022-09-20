package com.phoenix.web.security;

import com.phoenix.web.entity.SysPermission;
import com.phoenix.web.entity.SysUser;
import com.phoenix.web.service.SysPermissionService;
import com.phoenix.web.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过手机号获取用户信息和权限列表
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/17 15:00
 */
@Service
@Slf4j
@AllArgsConstructor
public class MobileUserDetailsService implements UserDetailsService {

    private SysUserService sysUserService;

    private SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        log.info("当前登录的手机号为: {}", mobile);
        //1.通过手机号查询用户信息
        SysUser sysUser = sysUserService.findByMobile(mobile);
        if (sysUser == null) {
            throw new UsernameNotFoundException("该手机未绑定用户");
        }
        //构建UserDetails
        //2.查询该用户的所有权限
        List<SysPermission> sysPermissionList = sysPermissionService.findPermissionByUserId(sysUser.getId());
        if (CollectionUtils.isEmpty(sysPermissionList)) {
            return sysUser;
        }
        //在左侧菜单动态渲染的时候会使用
        sysUser.setPermissions(sysPermissionList);
        //3.封装用户信息和权限信息
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (SysPermission sysPermission : sysPermissionList) {
            String code = sysPermission.getCode();
            grantedAuthorityList.add(new SimpleGrantedAuthority(code));
        }
        sysUser.setAuthorities(grantedAuthorityList);
        //SysUser实现了implements UserDetails，所以直接返回，spring security会自动判断user中的password和前端传入的password（经过passwordEncoder.encode(））是否一致
        //SysUser中的authorities是权限资源标识，spring security会自动判断用户是否合法
        //4.交给spring security自动进行身份认证
        return sysUser;
    }
}
