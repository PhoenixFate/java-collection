package com.phoenix.core.mobile;

import com.phoenix.core.authentication.CustomAuthenticationFailureHandler;
import com.phoenix.core.authentication.CustomAuthenticationSuccessHandler;
import com.phoenix.core.service.MobileUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

/**
 * 用于组合其他关于手机的登录的组件
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/17 15:45
 */
@Component
@AllArgsConstructor
public class MobileAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private MobileUserDetailsService mobileUserDetailsService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter();
        mobileAuthenticationFilter.setAuthenticationManager(
                builder.getSharedObject(AuthenticationManager.class));

        //默认为NullRememberService，需要重新setRememberService
        //容器中默认有一个PersistentTokenBasedRememberMeService实例
        //添加手机登录的记住我功能
        mobileAuthenticationFilter.setRememberMeServices(builder.getSharedObject(RememberMeServices.class));

        //添加手机登录的session认证策略，默认是 private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy()
        //需要自己赋值一个session认证策略, 用于管理用户session重复登录的限制
        mobileAuthenticationFilter.setSessionAuthenticationStrategy(builder.getSharedObject(SessionAuthenticationStrategy.class));

        //设置失败处理器
        mobileAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        //设置成功处理器
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        //构建一个MobileAuthenticationProvider实例，接收MobileUserDetailsService实例，通过手机号查询用户信息
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setUserDetailsService(mobileUserDetailsService);
        //将provider绑定到HttpSecurity上，并将手机号认证过滤器绑定到用户密码认证过滤器之后
        builder.authenticationProvider(mobileAuthenticationProvider)
                .addFilterAfter(mobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
