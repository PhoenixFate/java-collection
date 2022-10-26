package com.phoenix.blog.oauth2.config;

import com.phoenix.blog.common.constant.DreamBlogServerNameConstant;
import com.phoenix.blog.oauth2.security.JwtUser;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.concurrent.TimeUnit;

/**
 * 使用jwt token来管理令牌
 *
 * @Author phoenix
 * @Date 10/22/22 16:59
 * @Version 1.0
 */
@Configuration
@AllArgsConstructor
public class JwtTokenStoreConfig {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 非对称加密对应的JWT转换器
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        //创建jwt token转换器
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //采用非对称加密进行JWT令牌的加密
        //使用私钥进行加密：第一个参数为密钥证书文件，第二个参数为密钥库口令
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "oauth2".toCharArray());
        //通过别名获取key pair keyStoreKeyFactory.getKeyPair(alias)
        //keytool -genkeypair -alias oauth2 -keyalg RSA -keypass oauth2 -keystore oauth2.jks -storepass oauth2
        //别名为 oauth2，秘钥算法为 RSA，秘钥口令为 oauth2，秘钥库（文件）名称为 oauth2.jks，秘钥库（文件）口令为 oauth2。输入命令回车后，后面还问题需要回答，最后输入 y 表示确定
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
        //使用jwt管理令牌
        return new JwtTokenStore(jwtAccessTokenConverter()) {
            //redis存储jwt令牌
            @Override
            public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
                //将jwt中的唯一标识jti作为redis中的key值，value的值是accessToken访问令牌
                //jti === JWT ID : 声明为JWT提供了唯一的标识符
                if (token.getAdditionalInformation().containsKey("jti")) {
                    String jti = token.getAdditionalInformation().get("jti").toString();
                    //存储到redis中
                    JwtUser jwtUser = (JwtUser)authentication.getUserAuthentication().getPrincipal();
                    redisTemplate.opsForValue()
                            .set(DreamBlogServerNameConstant.DREAM_BLOG_AUTH+":accessToken:"+jwtUser.getUsername()+":"+ jti, token.getValue(), token.getExpiresIn(), TimeUnit.SECONDS);
                }
                super.storeAccessToken(token, authentication);
            }

            //删除redis中的jwt令牌
            @Override
            public void removeAccessToken(OAuth2AccessToken token) {
                //jti === JWT ID : 声明为JWT提供了唯一的标识符
                if (token.getAdditionalInformation().containsKey("jti")) {
                    String jti = token.getAdditionalInformation().get("jti").toString();
                    //将redis中对应的jti的记录删除
                    redisTemplate.delete(jti);
                }
                super.removeAccessToken(token);
            }
        };
    }

}
