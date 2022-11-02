package com.phoenix.blog.oauth2.config;

import com.phoenix.blog.oauth2.filter.ImageCodeValidateFilter;
import com.phoenix.blog.oauth2.handler.CustomAuthenticationFailureHandler;
import com.phoenix.blog.oauth2.handler.CustomAuthenticationSuccessHandler;
import com.phoenix.blog.oauth2.handler.CustomLogoutSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private final ImageCodeValidateFilter imageCodeValidateFilter;

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
        //采用token进行管理身份，而没有采用session，所以不需要创建HttpSession
        //配置当前的session管理方式：无session
        //SessionCreationPolicy.STATELESS: Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext
        //关闭csrf攻击

        // http.sessionManagement()
        //         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        //         .and()
        http.addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class) //将验证码过滤器添加到用户名密码过滤器之前
                .formLogin()
                .successHandler(customAuthenticationSuccessHandler) //自定义认证成功处理器
                .failureHandler(customAuthenticationFailureHandler) //自定义认证失败处理器
                .and()
                .logout().logoutSuccessHandler(customLogoutSuccessHandler) //自定义退出登录成功处理器
                .and()
                .csrf().disable();
    }
}
