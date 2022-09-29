package com.phoenix.oauth2.server.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * spring cloud oauth2 认证服务器配置类
 *
 * @Author phoenix
 * @Date 2022/9/29 16:30
 * @Version 1.0.0
 */
@Configuration
@EnableAuthorizationServer //开启认证服务器
@AllArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    /**
     * 配置被允许访问次认证服务器的客户端信息
     * 1.内存方式
     * 2.数据库方式
     *
     * @param clients 客户端
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("spring-security-pc") //客户端id
                .secret(passwordEncoder.encode("pc-secret")) //客户端的密码
                .resourceIds("product-server") //资源id，限制客户端访问的微服务的名称
                .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token") //授权方式(可以指定多种方式)：授权码模式、密码模式、简化模式、客户端模式
                .scopes("all") //授权范围标识，哪部分资源可以访问（all只是标识，并不代表所有资源）
                .autoApprove(false) //false: 跳转到一个授权页面手动点击授权；true: 不需要手动点授权，直接响应一个授权码
                .redirectUris("http://www.mengxuegu.com") //客户端回调地址
        ;
    }
}
