package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
        //??????????????????
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        //??????????????????cookie
        this.saveCookie(authToken.getAccess_token());
        return new LoginResult(CommonCode.SUCCESS, authToken.getAccess_token());
    }

    //??????????????????cookie
    //HttpServletResponse response,String domain,String path, String name,String value, int maxAge,boolean httpOnly
    private void saveCookie(String token) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, cookieMaxAge, false);
    }

    @Override
    @PostMapping("user/logout")
    public ResponseResult logout() {
        //??????cookie????????????????????????
        String token = getTokenFormCookie();
        //??????redis??????token
        authService.logout(token);
        //??????cookie
        this.clearCookie(token);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("user/jwt")
    public JwtResult getUserJwt() {
        //??????cookie????????????????????????
        String uid = getTokenFormCookie();
        if (uid == null) {
            return new JwtResult(CommonCode.FAIL, null);
        }
        //??????????????????redis?????????jwt??????
        AuthToken userToken = authService.getUserToken(uid);
        if (userToken != null) {
            //???jwt?????????????????????
            String jwt_token = userToken.getJwt_token();
            return new JwtResult(CommonCode.SUCCESS, jwt_token);
        }
        return null;
    }

    //??????cookie??????????????????
    private String getTokenFormCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        if (map != null && map.get("uid") != null) {
            String uid = map.get("uid");
            return uid;
        }
        return null;
    }

    //???cookie??????token
    private void clearCookie(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //HttpServletResponse response,String domain,String path, String name, String value, int maxAge,boolean httpOnly
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
    }


}
