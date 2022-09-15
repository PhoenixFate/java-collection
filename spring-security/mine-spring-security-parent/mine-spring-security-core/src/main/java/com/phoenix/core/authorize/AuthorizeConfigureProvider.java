package com.phoenix.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权配置统一接口，所有模块的授权配置类都要实现这个接口
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/15 13:42
 */
public interface AuthorizeConfigureProvider {

    void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
