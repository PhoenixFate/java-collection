package com.phoenix.web.security.authorize;

import com.phoenix.core.authorize.AuthorizeConfigureProvider;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * 关于系统管理模块的授权配置
 */
@Component
public class SystemAuthorizeConfigureProvider implements AuthorizeConfigureProvider {

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers("/user").hasAnyRole("ADMIN", "MANAGER") //设置角色时会加上ROLE_作为前缀，所以在UserDetails中赋值role的时候需要添加前缀
                .antMatchers("/role").hasAnyAuthority("sys:user", "sys:role") //有特定标识符才能访问例如 sys:user:list
                // .antMatchers("/user").hasIpAddress("192.168.1.1/29") //限制指定ip或者指定范围内的ip才能访问
                .antMatchers(HttpMethod.GET, "/permission").access("hasAuthority('sys:permission') or hasAnyRole('ADMIN','MANAGER')");

    }
}
