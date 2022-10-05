package com.phoenix.oauth2.server.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

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

    /**
     * 管理令牌
     *
     * @return RedisTokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        //redis管理令牌
        // return new RedisTokenStore(redisConnectionFactory);

        //使用jdbc管理令牌
        return new JdbcTokenStore(dataSource);
    }

}
