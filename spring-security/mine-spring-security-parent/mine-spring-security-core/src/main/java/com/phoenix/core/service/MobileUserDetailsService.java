package com.phoenix.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class MobileUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        //1.通过手机号查询用户信息
        String username = "phoenix";
        //2.如果有用户信息，则再获取权限资源

        //3.封装用户信息
        return new User(username, "", true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("sys:user,sys:role,sys:user:add,ROLE_ADMIN"));
    }
}
