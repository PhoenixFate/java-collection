package com.phoenix.blog.question.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 资源服务器配置类
 *
 * @Author phoenix
 * @Date 10/24/22 23:40
 * @Version 1.0
 */
@Configuration
@EnableResourceServer //标识为资源服务器：所有发往这个服务的请求，都会去请求头中找access_token，找不到或者通过认证服务器验证不合法，则不允许访问
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级权限控制
@AllArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    /**
     * 指定jwt令牌管理方式
     *
     * @param resources configurer for the resource server
     * @throws Exception 异常
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //采用jwt管理令牌
        resources.tokenStore(tokenStore);
        //配置resourceId
        resources.resourceId("question-resource-id");
    }

    /**
     * 资源服务器的安全配置
     * 权限规则配置，指定哪些接口需要认证后才能访问，哪些接口不需要认证就可以访问
     *
     * @param http the current http filter configuration
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //采用token进行管理身份，而没有采用session，所以不需要创建HttpSession
        //配置当前的session管理方式：无session
        //SessionCreationPolicy.STATELESS: Spring Security will never create an HttpSession and it will never use it to obtain the SecurityContext
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests() //请求的授权配置
                // 放行 swagger-ui 相关请求
                .antMatchers(
                        "/v2/*",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                // 放行 /api 开头的请求
                .antMatchers("/api/**").permitAll()
                // 所有请求，都需要有all范围（scope）
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                // 其他所有请求都要先通过认证
                .anyRequest().authenticated()
        ;

    }
}
