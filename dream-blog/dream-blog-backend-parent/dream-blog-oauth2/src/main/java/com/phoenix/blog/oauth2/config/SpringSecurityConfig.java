package com.phoenix.blog.oauth2.config;

import com.phoenix.blog.oauth2.handler.CustomAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 安全配置类
 *
 * @Author phoenix
 * @Date 10/21/22 20:17
 * @Version 1.0
 */
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //读取用户信息认证
        //指定使用自定义查询用户信息来完成身份认证
        auth.userDetailsService(userDetailsService);
    }

    @Bean // password模式需要该bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭csrf攻击
        http.formLogin()
                .successHandler(customAuthenticationSuccessHandler) //自定义认证成功处理器
                .and()
                .csrf().disable();

    }
}
