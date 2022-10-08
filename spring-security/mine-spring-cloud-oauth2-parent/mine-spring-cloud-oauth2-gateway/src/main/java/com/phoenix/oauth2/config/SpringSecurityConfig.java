package com.phoenix.oauth2.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author phoenix
 * @Date 2022/10/8 16:43
 * @Version 1.0.0
 */
@EnableWebSecurity //启动spring security 的安全配置拦截
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 将所有请求放行，交给资源配置类进行资源权限判断(即把所有请求交给ResourceServerConfig)
     * 默认会拦截所有请求
     *
     * @param http http
     * @throws Exception 异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //放行所有请求
        http.authorizeRequests().anyRequest().permitAll();
    }
}
