package com.phoenix.core.mobile;

import com.phoenix.core.authentication.CustomAuthenticationFailureHandler;
import com.phoenix.core.authentication.CustomAuthenticationSuccessHandler;
import com.phoenix.core.service.MobileUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
