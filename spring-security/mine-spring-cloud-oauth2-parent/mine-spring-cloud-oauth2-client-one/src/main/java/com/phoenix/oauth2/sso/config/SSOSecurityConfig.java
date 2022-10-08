package com.phoenix.oauth2.sso.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * @Author phoenix
 * @Date 10/7/22 14:51
 * @Version 1.0
 */
@Configuration
@EnableOAuth2Sso //开启单点登录功能
public class SSOSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 当客户端要请求资源服务器中的资源时，我们需要带上令牌给资源服务器，由于我们使用了 @EnableOAuth2Sso
     * 注解，SpringBoot 会在请求上下文中添加一个 OAuth2ClientContext 对象，而我们只要在配置类中向容器中添加
     * 一个 OAuth2RestTemplate 对象，请求的资源服务器时就会把令牌带上转发过去。
     * @param factory UserInfoRestTemplateFactory
     * @return OAuth2RestTemplate
     */
    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(UserInfoRestTemplateFactory factory) {
        return factory.getUserInfoRestTemplate();
    }


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
