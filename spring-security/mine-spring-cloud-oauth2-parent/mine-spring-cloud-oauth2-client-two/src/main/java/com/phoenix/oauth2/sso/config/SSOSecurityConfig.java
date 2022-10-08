package com.phoenix.oauth2.sso.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author phoenix
 * @Date 10/7/22 14:51
 * @Version 1.0
 */
@Configuration
@EnableOAuth2Sso //开启单点登录功能
public class SSOSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //首页所有人都可以访问
                .antMatchers("/").permitAll()
                //其他请求需要认证后才能访问
                .anyRequest().authenticated()
                .and()
                .logout()
                //请求认证服务器将用户进行退出
                // .logoutSuccessUrl("http://localhost:8643/auth/logout")//当应用退出后，会交给某个处理
                .logoutSuccessUrl("http://localhost:7001/auth/logout")//当应用退出后，会交给某个处理 (接入网关之后，使用网关地址)
                .and()
                .csrf().disable()
        ;

    }
}
