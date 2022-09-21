package com.phoenix.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/13 15:04
 */
@Configuration
@Order(-10)
public class FirstNeedConfig {

    /**
     * 为了解决退出重新登录的问题
     * session 注册,spring security 中默认也是SessionRegistryImpl
     *
     * @return SessionRegistryImpl
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //BCryptPasswordEncoder: 密码加密器，根据密码的值，随机生成一个盐值，然后和密码一起加密
        return new BCryptPasswordEncoder();
    }
}
