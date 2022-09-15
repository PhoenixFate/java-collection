package com.phoenix.core.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 将所有的授权配置统一管理起来（即AuthorizeConfigureProvider的实现类统一管理起来）
 */
@Component
public class CustomAuthorizeConfigureManager implements AuthorizeConfigureManager {

    @Autowired
    List<AuthorizeConfigureProvider> authorizeConfigureProviderList;

    /**
     * 将一个个AuthorizeConfigureProvider的实现类，传入配置的参数
     *
     * @param config 配置
     */
    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for (AuthorizeConfigureProvider authorizeConfigureProvider : authorizeConfigureProviderList) {
            authorizeConfigureProvider.configure(config);
        }
    }
}
