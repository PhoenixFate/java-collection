package com.phoenix.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 查询数据库中的用户信息
 * 实现spring security提供的UserDetailsService接口
 */
@Slf4j
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("--------- 请求认证的用户名： " + username);

        //1.通过请求的用户名去数据库中查询用户信息
        if (!"phoenix".equals(username)) {
            throw new UsernameNotFoundException("用户名或者密码错误");
        }
        //这里面模拟查询数据库中的加密后的密码
        String password = new BCryptPasswordEncoder().encode("123456");
        //构建UserDetails
        //2.查询该用户的所有权限

        //3.封装用户信息和权限信息
        return new User(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("sys:user,sys:role,sys:user:add,ROLE_ADMIN"));
    }

}
