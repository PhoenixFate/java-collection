package com.phoenix.oauth2.config;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author phoenix
 * @Date 10/3/22 11:37
 * @Version 1.0
 */
@Configuration
@AllArgsConstructor
public class TokenConfig {

    /**
     * 使用非对称加密创建JWT转换器
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //提取公钥
        ClassPathResource resource = new ClassPathResource("public_key.txt");
        String publicKey = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        //设置公钥
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        return jwtAccessTokenConverter;
    }

    /**
     * 管理令牌
     *
     * @return RedisTokenStore
     */
    @Bean
    public TokenStore tokenStore() throws IOException {
        //使用jwt管理令牌
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

}