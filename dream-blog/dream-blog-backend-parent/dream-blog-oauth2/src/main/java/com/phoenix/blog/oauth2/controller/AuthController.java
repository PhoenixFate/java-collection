package com.phoenix.blog.oauth2.controller;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.phoenix.blog.common.base.Result;
import com.phoenix.blog.common.util.RequestUtil;
import com.phoenix.blog.oauth2.service.AuthService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author phoenix
 * @Date 10/23/22 17:50
 * @Version 1.0
 */
@RestController
@AllArgsConstructor
public class AuthController {

    private static final String HEADER_TYPE = "Basic ";

    private final ClientDetailsService clientDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    @GetMapping("/user/refreshToken")
    public Result refreshToken(HttpServletRequest request) throws IOException, HttpProcessException {
        //获取请求中的刷新令牌
        String refreshToken = request.getParameter("refreshToken");
        Preconditions.checkArgument(StringUtils.isNotEmpty(refreshToken), "刷新令牌不能为空");

        //获取请求头
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
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
        //获取新的认证信息
        return authService.refreshToken(header, refreshToken);
    }

}
