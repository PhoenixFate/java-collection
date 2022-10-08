package com.phoenix.oauth2.resource.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author phoenix
 * @Date 2022/10/8 16:43
 * @Version 1.0.0
 */
@EnableWebSecurity //启动spring security 的安全配置拦截
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级别权限控制 默认为false
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


}
