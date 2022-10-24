package com.phoenix.blog.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.util.RequestUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 自定义认证成功处理器：认证成功后响应json数据给前端
 *
 * @Author phoenix
 * @Date 10/24/22 00:56
 * @Version 1.0
 */
@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String HEADER_TYPE = "Basic ";

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("登录成功 {}", authentication.getPrincipal());
        //获取请求头中的客户端信息
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("header {}", header);
        //获取请求头
        if (header == null || !header.startsWith(HEADER_TYPE)) {
            throw new UnsupportedOperationException("请求头中无client信息");
        }
        //解析请求头的客户端信息
        String[] tokens = RequestUtil.extractAndDecodeHeader(header);
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];
        //查询客户端信息，核对是否有效
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnsupportedOperationException("clientId对应的配置信息不存在");
        }
        //校验客户端密码是否有效
        if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnsupportedOperationException("无效的clientSecret");
        }

        //组合请求对象，去获取对象
        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(), clientId, clientDetails.getScope(), "custom");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        //获取访问令牌
        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        //向前端回写json
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(Result.ok(accessToken)));
    }
}
