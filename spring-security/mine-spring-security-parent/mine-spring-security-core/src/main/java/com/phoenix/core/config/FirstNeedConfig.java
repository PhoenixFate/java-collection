package com.phoenix.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

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

}
