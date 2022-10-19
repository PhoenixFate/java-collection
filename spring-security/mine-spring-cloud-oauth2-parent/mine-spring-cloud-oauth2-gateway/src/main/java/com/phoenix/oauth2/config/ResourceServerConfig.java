package com.phoenix.oauth2.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;


/**
 * 当前类用于管理所有的资源：认证资源、商品服务
 * <p>
 * 在Spring Security的FilterChain中，OAuth2AuthenticationProcessingFilter在FilterSecurityInterceptor的前面，
 * 所以会先验证client有没有此resource的权限，只有在有此resource的权限的情况下，才会再去做进一步的进行其他验证的判断
 *
 * @Author phoenix
 * @Date 2022/10/8 16:32
 * @Version 1.0.0
 * 认证资源服务器的配置
 * 商品资源服务器的配置
 */

@Configuration
@AllArgsConstructor
public class ResourceServerConfig {

    private final TokenStore tokenStore;

    /**
     * 认证资源服务器的配置
     * 商品资源服务器的配置
     */

    @Configuration
    @EnableResourceServer //标识为资源服务器
    @Order(2)
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
            //http.authorizeRequests()
            //        //.anyRequest().permitAll()
            //        .antMatchers("/auth/**").permitAll()
            //;

            http.csrf().disable()
                    //前后端分离，禁用session
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    //认证服务的请求全部放行
                    .and().authorizeRequests().antMatchers("/auth/**").permitAll()
            ;
        }
    }


    /**
     * 商品资源服务器的配置
     */

    @Configuration
    @EnableResourceServer //标识为资源服务器
    @Order(1)
    public class ProductResourceServerConfig extends ResourceServerConfigurerAdapter {

        //配置多个resource_id无效了，待解决
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

