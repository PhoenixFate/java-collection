package com.phoenix.oauth2.server.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * spring cloud oauth2 认证服务器配置类
 *
 * @Author phoenix
 * @Date 2022/9/29 16:30
 * @Version 1.0.0
 */
@Configuration
@EnableAuthorizationServer //开启认证服务器
@AllArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    //刷新令牌的时候需要使用UserDetailsService
    private final UserDetailsService customUserDetailsService;

    //token管理方式，在TokenConfig类中已经添加到容器中了
    private final TokenStore tokenStore;

    private final DataSource dataSource;

    //jwt token转换器，用于使用jwtToken之后的加解密
    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 授权码管理策略：
     * 该类可以将授权码code存入数据库（默认在内存中）
     * 授权码授权变成token会删除数据库中的token
     *
     * @return JdbcAuthorizationCodeServices
     */
    @Bean
    public AuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * //使用jdbc管理客户端信息需要该JdbcClientDetailsService类的实例
     *
     * @return JdbcClientDetailsService
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 配置被允许访问次认证服务器的客户端信息
     * 1.内存方式
     * 2.数据库方式
     *
     * @param clients 客户端
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //使用内存方式配置oauth客户端信息
        // clients.inMemory()
        //         .withClient("spring-security-pc") //客户端id
        //         .secret(passwordEncoder.encode("pc-secret")) //客户端的密码
        //         .resourceIds("product-server") //资源id，限制客户端访问的微服务的名称
        //         //授权方式(可以指定多种方式)：授权码模式、密码模式、简化模式、客户端模式;
        //         //refresh_token指可以通过刷新令牌重新获取令牌
        //         //如果没有指定refresh_token,则http://127.0.0.1:8643/auth/oauth/token返回的数据库中没有refresh_token
        //         .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
        //         .scopes("all", "read", "write", "中文测试") //授权范围标识，哪部分资源可以访问（all只是标识，并不代表所有资源）
        //         .autoApprove(false) //false: 跳转到一个授权页面然后手动点击授权；true: 不需要手动点授权，直接响应一个授权码
        //         .redirectUris("http://127.0.0.1:8643/auth") //客户端回调地址
        //         //更改accessToken默认过期时间2天=60*60*24*2
        //         .accessTokenValiditySeconds(60 * 60 * 24 * 2)//访问令牌有效时长，默认为12小时
        //         .refreshTokenValiditySeconds(60 * 60 * 24 * 60) //刷新令牌有效时长，默认为30天
        //         //配置多个客户端
        //         .and()
        //         .withClient("spring-security-app") //客户端id
        //         .secret(passwordEncoder.encode("pc-secret")) //客户端的密码
        //         .resourceIds("product-server") //资源id，限制客户端访问的微服务的名称
        //         //授权方式(可以指定多种方式)：授权码模式、密码模式、简化模式、客户端模式;
        //         //refresh_token指可以通过刷新令牌重新获取令牌
        //         //如果没有指定refresh_token,则http://127.0.0.1:8643/auth/oauth/token返回的数据库中没有refresh_token
        //         .authorizedGrantTypes("authorization_code", "password", "implicit", "client_credentials", "refresh_token")
        //         .scopes("all", "read", "write", "中文测试") //授权范围标识，哪部分资源可以访问（all只是标识，并不代表所有资源）
        //         .autoApprove(false) //false: 跳转到一个授权页面然后手动点击授权；true: 不需要手动点授权，直接响应一个授权码
        //         .redirectUris("http://127.0.0.1:8643/auth") //客户端回调地址
        //         //更改accessToken默认过期时间2天=60*60*24*2
        //         .accessTokenValiditySeconds(60 * 60 * 24 * 2)//访问令牌有效时长，默认为12小时
        //         .refreshTokenValiditySeconds(60 * 60 * 24 * 60) //刷新令牌有效时长，默认为30天
        // ;


        //使用jdbc管理客户端信息
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 关于认证服务器端点的配置
     *
     * @param endpoints 端点
     * @throws Exception 异常
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //oauth2 password模式需要AuthenticationManager的实例
        endpoints.authenticationManager(authenticationManager);
        //刷新令牌需要使用userDetailsService
        endpoints.userDetailsService(customUserDetailsService);
        //endpoints添加令牌管理方式
        endpoints.tokenStore(tokenStore);
        //使用JWT令牌之后需要配置JWT转换器
        endpoints.accessTokenConverter(jwtAccessTokenConverter);
        //授权码管理策略：会将产生的授权码放到oauth_code表中，如果这个授权码已经使用了，则对应这个表中的数据库就会被删除
        //授权码管理策略：TokenConfig中的jdbcAuthorizationCodeServices
        endpoints.authorizationCodeServices(jdbcAuthorizationCodeServices());
    }

    /**
     * 令牌端点的安全配置
     *
     * @param security AuthorizationServerSecurityConfigurer
     * @throws Exception 异常
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //配置/oauth/token_key所有人可以访问 后面要获取公钥
        security.tokenKeyAccess("permitAll()");//默认是拒绝访问tokenKeyAccess = "denyAll()";
        //配置/oauth/check_token认证后可以访问，用于检查token是否有效
        security.checkTokenAccess("isAuthenticated()");//默认拒绝访问checkTokenAccess = "denyAll()"
    }
}
