package com.phoenix.core.authentication.session;

import com.alibaba.fastjson.JSONObject;
import com.phoenix.core.authentication.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 当同一用户的session达到指定数量时候，所执行的策略，也就是会执行该类
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/29 15:05
 */

public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    public CustomSessionInformationExpiredStrategy(CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        //1.获取用户名
        UserDetails principal = (UserDetails) event.getSessionInformation().getPrincipal();
        //新建一个spring security AuthenticationException 异常类，用于给自定义的失败处理器
        AuthenticationException authenticationException = new AuthenticationServiceException(String.format("[%s] 您的账号已经在别的地方登录，当前登录已失效。如果密码遭到泄露，请立即修改密码！", principal.getUsername()));
        try {
            event.getRequest().setAttribute("toAuthentication", true);
            customAuthenticationFailureHandler.onAuthenticationFailure(event.getRequest(), event.getResponse(), authenticationException);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }


}
