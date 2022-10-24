package com.phoenix.blog.article.config;

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
 * @Date 10/24/22 23:03
 * @Version 1.0
 */
@Configuration
public class JwtTokenStoreConfig {

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() throws IOException {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //采用的是非对称加密，资源服务器要使用公钥解密，公钥存放在public_key.txt
        ClassPathResource classPathResource = new ClassPathResource("public_key.txt");
        String publicKey = IOUtils.toString(classPathResource.getInputStream(), StandardCharsets.UTF_8);
        //设置公钥
        jwtAccessTokenConverter.setVerifierKey(publicKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() throws IOException {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


}
