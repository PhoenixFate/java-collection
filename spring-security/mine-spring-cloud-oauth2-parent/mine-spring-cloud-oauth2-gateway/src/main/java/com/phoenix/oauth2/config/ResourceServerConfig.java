package com.phoenix.oauth2.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 当前类用于管理所有的资源：认证资源、商品服务
 *
 * @Author phoenix
 * @Date 2022/10/8 16:32
 * @Version 1.0.0
 */
@Configuration
public class ResourceServerConfig {

    @Autowired
    private TokenStore tokenStore;


    /**
     * 认证资源服务器的配置
     */
    @Configuration
    @EnableResourceServer //标识为资源服务器
    public class AuthResourceServerConfig extends ResourceServerConfigurerAdapter {

        public static final String RESOURCE_ID = "auth-server";

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            //当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源的id的权限
            resources.resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore) //使用JWT令牌
            ;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            //关于请求认证服务器的资源，则所有请求放行
            http.authorizeRequests()
                    .anyRequest().permitAll();
        }
    }

    /**
     * 商品资源服务器的配置
     */
    @Configuration
    @EnableResourceServer //标识为资源服务器
    public class ProductResourceServerConfig extends ResourceServerConfigurerAdapter {

        public static final String RESOURCE_ID = "product-server";

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            //当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源的id的权限
            resources.resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore) //使用JWT令牌
            ;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/product/**")
                    .access("#oauth2.hasScope('PRODUCT_API')");
        }
    }


}
