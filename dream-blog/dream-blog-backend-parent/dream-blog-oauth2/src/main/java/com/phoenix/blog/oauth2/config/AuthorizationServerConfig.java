package com.phoenix.blog.oauth2.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 认证服务器
 * 标识当前资源为认证服务器
 *
 * @Author phoenix
 * @Date 10/22/22 17:22
 * @Version 1.0
 */
@Configuration
@EnableAuthorizationServer // 标识当前微服务为认证服务器
@AllArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    //SpringSecurityBean的authenticationManagerBean()方法会创建该bean
    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    private final JwtTokenEnhancer jwtTokenEnhancer;

    /**
     * 创建ClientDetailsService：基于数据库的ClientDetailsService
     * 用户管理所有可以访问的客户端（相对于认证服务器的客户端）
     *
     * @return JdbcClientDetailsService
     */
    @Bean //客户端使用jdbc管理（表oauth_client_details）
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 配置被允许访问认证服务的客户端信息：数据库方式管理客户端信息
     *
     * @param clients the client details configurer
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }


    /**
     * 关于认证服务器 端点 配置
     *
     * @param endpoints the endpoints configurer
     * @throws Exception 异常
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //密码模式必须使用authenticationManager实例
        endpoints.authenticationManager(authenticationManager);
        //刷新令牌需要使用userDetailsService
        endpoints.userDetailsService(userDetailsService);
        //令牌的管理方式
        endpoints.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter);
        //添加token增强器
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        //组合jwt增强器和jwt转换器
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));
        //将认证信息的增强器添加到端点上
        endpoints.tokenEnhancer(tokenEnhancerChain);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //配置/oauth/token_key所有人可以访问 用于获取公钥
        security.tokenKeyAccess("permitAll()");//默认是拒绝访问tokenKeyAccess = "denyAll()";
        //配置/oauth/check_token认证后可以访问，用于检查token是否有效
        security.checkTokenAccess("isAuthenticated()");//默认拒绝访问checkTokenAccess = "denyAll()"

    }
}
