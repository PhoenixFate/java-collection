package com.phoenix.blog.oauth2.config;

import com.alibaba.fastjson.JSON;
import com.phoenix.blog.oauth2.security.JwtUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展响应的认证信息
 *
 * @Author phoenix
 * @Date 10/23/22 12:18
 * @Version 1.0
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", JSON.toJSON(jwtUser));
        //设置附加信息
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);

        return accessToken;
    }
}
