package com.phoenix.workflow.security;

import com.phoenix.workflow.entity.SysUser;
import com.phoenix.workflow.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义的UserDetail；类似于shiro中的UserRealm
 */
@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * srping security 会调用该方法
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //SysUser 实现了UserDetails
        SysUser sysUser = sysUserService.findByUserName(username);
        if(sysUser==null){
            throw new UsernameNotFoundException("用户名或者密码错误");
        }

        Set<GrantedAuthority> authoritySet=new HashSet<>();
        //添加用户拥有的角色 ACTIVITI_USER（activiti7固定要求），才能使用ProcessRuntime/TaskRuntime
        // 候选组MANAGER_TEAM
        authoritySet.add(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"));
        authoritySet.add(new SimpleGrantedAuthority("GROUP_MANAGER_TEAM"));
        sysUser.setAuthorities(authoritySet);
        //SysUser 实现了UserDetails
        return sysUser;
    }
}
