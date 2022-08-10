package com.phoenix.core.authentication;

import com.phoenix.base.constant.LoginResponseType;
import com.phoenix.base.result.RequestResult;
import com.phoenix.core.property.SpringSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * spring security 认证失败处理器
 * <p>
 * 实现AuthenticationFailureHandler接口，用于自定义返回认证失败的内容
 * 继承SimpleUrlAuthenticationFailureHandler
 * 用于自定义返回认证失败的内容，或者调用super.onAuthenticationFailure(request, response, exception)
 * 重定向到登录页面
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/10 14:20
 */
@Component("customAuthenticationFailureHandler")
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SpringSecurityProperties springSecurityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //根据自己的配置来返回JSON还是重定向redirect
        if (LoginResponseType.JSON.getType().equals(springSecurityProperties.getAuthentication().getLoginReturnType())) {
            //认证失败响应json字符串
            RequestResult result = RequestResult.build(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(result.toJsonString());
        } else {
            //设置默认登录失败的页面，后面加？error用于展现登录错误
            super.setDefaultFailureUrl(springSecurityProperties.getAuthentication().getLoginPage() + "?error");
            //调用父类方法，重定向到登录页面
            super.onAuthenticationFailure(request, response, exception);
        }

    }

}
