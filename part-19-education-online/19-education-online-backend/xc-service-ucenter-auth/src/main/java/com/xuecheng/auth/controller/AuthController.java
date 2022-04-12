package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {

    @Value("${xuecheng.auth.clientId}")
    private String clientId;

    @Value("${xuecheng.auth.clientId}")
    private String clientSecret;

    @Value("${xuecheng.auth.cookieDomain}")
    private String cookieDomain;

    @Value("${xuecheng.auth.cookieMaxAge}")
    private Integer cookieMaxAge;

    @Autowired
    private AuthService authService;

    @Override
    @PostMapping("user/login")
    public LoginResult login(LoginRequest loginRequest) {
        if (loginRequest == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if (StringUtils.isEmpty(loginRequest.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        //申请身份令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        //将令牌存储到cookie
        this.saveCookie(authToken.getAccess_token());
        return new LoginResult(CommonCode.SUCCESS, authToken.getAccess_token());
    }

    //将令牌存储到cookie
    //HttpServletResponse response,String domain,String path, String name,String value, int maxAge,boolean httpOnly
    private void saveCookie(String token) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    @Override
    @PostMapping("user/logout")
    public ResponseResult logout() {
        return null;
    }
}
