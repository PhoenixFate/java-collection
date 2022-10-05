package com.phoenix.oauth2.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @Author phoenix
 * @Date 10/5/22 23:48
 * @Version 1.0
 */
@Configuration
@EnableResourceServer //资源服务器的注解: 标识为资源服务器，开启之后就需要带着token来访问资源
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启方法级别权限控制 默认为false
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "product-server";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源的id的权限
        resources.resourceId(RESOURCE_ID)
                .tokenServices(tokenServices())
        ;
    }

    public ResourceServerTokenServices tokenServices() {
        //远程认证服务器进行校验 token是否有效
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        //请求认证服务器校验地址，默认情况下，这个地址在认证服务器是拒绝访问的，需修改认证服务器中的校验地址为通过验证可访问
        remoteTokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:8643/auth/oauth/check_token");
        remoteTokenServices.setClientId("spring-security-pc");
        remoteTokenServices.setClientSecret("pc-secret");
        return remoteTokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //关闭session机制，只是使用token访问
        //SpringSecurity不会使用也不会创建HttpSession实例
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //配置某些资源对应的scope范围
                .authorizeRequests()
                //先匹配原则，匹配上则放行
                .antMatchers("/product/list").hasAuthority("sys:user:list")
                //所有请求都需要 针对客户端配置的all（scope）范围
                .antMatchers("/**").access("#oauth2.hasScope('all')")
        ;

    }
}
