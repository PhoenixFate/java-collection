package com.phoenix.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 一个配置，指定为springSecurity的配置，需要继承WebSecurityConfigurerAdapter
 */
@Configuration
@EnableWebSecurity //开启spring security过滤器链 filter
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 认证管理器：
     * 1.认证信息（用户名、密码）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    /**
     * 资源权限配置
     * 1.被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic() //采用httpBasic认证方式
                .and()
                .authorizeRequests() //认证请求
                .anyRequest().authenticated() //所有访问该应用的http请求都需要通过身份认证才可以访问

        ;


    }
}
