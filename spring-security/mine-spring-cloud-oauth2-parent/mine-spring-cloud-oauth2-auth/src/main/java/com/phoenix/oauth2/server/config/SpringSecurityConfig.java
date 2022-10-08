package com.phoenix.oauth2.server.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * spring security安全配置类
 *
 * @Author phoenix
 * @Date 2022/9/29 16:49
 * @Version 1.0.0
 */
@EnableWebSecurity //自带类@Configuration注解
@AllArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    //private final PasswordEncoder passwordEncoder;

    private final UserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //采用内存配置的方式
        // auth.inMemoryAuthentication()
        //         .withUser("admin").password(passwordEncoder.encode("123456"))
        //         .authorities("product");//指定权限标识
        auth.userDetailsService(customUserDetailsService);
    }

    /**
     * oauth2 password模式需要此bean
     *
     * @return AuthenticationManager
     * @throws Exception 异常
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
