package com.phoenix.oauth2.server.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;

/**
 * @Author phoenix
 * @Date 10/3/22 11:37
 * @Version 1.0
 */
@Configuration
@AllArgsConstructor
public class TokenConfig {

    //采用redis管理token需要用到 （正常使用jdbc管理token）
    // private final RedisConnectionFactory redisConnectionFactory;

    private final DataSource dataSource;

    // /**
    //  * 数据库连接池
    //  *
    //  * @return DruidDataSource
    //  */
    // @ConfigurationProperties(prefix = "spring.datasource")
    // @Bean
    // public DataSource dataSource() {
    //     return new DruidDataSource();
    // }

    public static final String SIGNING_KEY = "test-key";

    /**
     * 对称式的加密对应的JWT转换器
     *
     * @return JwtAccessTokenConverter
     */
    // @Bean
    // public JwtAccessTokenConverter jwtAccessTokenConverter() {
    //     JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    //     //使用对称加密进行签名令牌，资源服务器也要采用此迷药来进行解密
    //     jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);
    //     return jwtAccessTokenConverter;
    // }

    /**
     * 非对称加密对应的JWT转换器
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //非对称加密进行JWT令牌的加密
        //使用私钥进行加密：第一个参数为密钥证书文件，第二个参数为密钥库口令
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "oauth2".toCharArray());
        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2"));
        return jwtAccessTokenConverter;
    }

    /**
     * 告诉spring使用jwt来管理令牌
     *
     * @return RedisTokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        //redis管理令牌
        // return new RedisTokenStore(redisConnectionFactory);

        //使用jdbc管理令牌
        // return new JdbcTokenStore(dataSource);

        //使用jwt管理令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

}
