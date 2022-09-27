package com.phoenix.core.authentication;

import com.alibaba.fastjson.JSON;
import com.phoenix.base.constant.LoginResponseType;
import com.phoenix.base.result.RequestResult;
import com.phoenix.core.property.SpringSecurityAuthenticationProperties;
import com.phoenix.core.property.SpringSecurityProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security 认证成功处理器
 * 1. 可以定义响应json还是跳转页面，或者认证成功后进行其他处理
 * <p>
 * 如果是实现AuthenticationSuccessHandler接口，可以实现自定义返回内容
 * 如果是继承SavedRequestAwareAuthenticationSuccessHandler，可以调用
 * super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication) 来重定向到上一个请求页面
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/10 14:05
 */
@Slf4j
@Component("customAuthenticationSuccessHandler")
@AllArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SpringSecurityProperties springSecurityProperties;

    private final AuthenticationSuccessListener authenticationSuccessListener;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        if (authenticationSuccessListener != null) {
            //当认证成功之后，调用此监听，进行后续处理，比如加载用户权限菜单
            authenticationSuccessListener.successListener(httpServletRequest, httpServletResponse, authentication);
        }

        //根据配置返回JSON还是重定向
        if (LoginResponseType.JSON.getType().equals(springSecurityProperties.getAuthentication().getLoginReturnType())) {
            //认证成功后，响应json字符串
            RequestResult result = RequestResult.ok("认证成功");
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(result.toJsonString());
        } else {
            // 重定向到上次请求的地址上，引发跳转到认证页面的地址
            log.info("authentication: " + JSON.toJSONString(authentication));
            super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }
    }
}
