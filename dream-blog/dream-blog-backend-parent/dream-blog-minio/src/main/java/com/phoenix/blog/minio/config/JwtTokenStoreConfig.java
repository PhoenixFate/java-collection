package com.phoenix.blog.minio.config;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
        //将定义的转换器对象添加到jwt转换器中
        jwtAccessTokenConverter.setAccessTokenConverter(new CustomAccessTokenConverter());
        return jwtAccessTokenConverter;
    }

    @Bean
    public TokenStore tokenStore() throws IOException {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 定制AccessToken转换器，用于为额外添加的用户信息在资源服务器中获取
     * static内部类是内部类中一个比较特殊的情况，Java文档中是这样描述static内部类的：一旦内部类使用static修饰，那么此时这个内部类就升级为顶级类
     */
    private static class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);
            oAuth2Authentication.setDetails(map);
            return oAuth2Authentication;
        }
    }

}
