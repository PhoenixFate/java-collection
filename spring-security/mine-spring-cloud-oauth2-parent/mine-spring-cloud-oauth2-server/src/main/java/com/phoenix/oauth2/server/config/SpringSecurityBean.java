package com.phoenix.oauth2.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 管理与spring security相关的配置类
 *
 * @Author phoenix
 * @Date 2022/9/29 16:35
 * @Version 1.0.0
 */
@Configuration
public class SpringSecurityBean {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
