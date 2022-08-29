package com.phoenix.core.authentication;

import com.alibaba.fastjson.JSONObject;
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

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        //1.获取用户名
        UserDetails principal = (UserDetails) event.getSessionInformation().getPrincipal();
        //新建一个spring security AuthenticationException 异常类，用于给自定义的失败处理器
        AuthenticationException authenticationException = new AuthenticationServiceException(String.format("[%s] 该用户在另一台电脑上登录，您已被下线", principal.getUsername()));
        try {
            event.getRequest().setAttribute("toAuthentication", true);
            customAuthenticationFailureHandler.onAuthenticationFailure(event.getRequest(), event.getResponse(), authenticationException);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "老王");
        map.put("id", "195");

        String s = JSONObject.toJSONString(map);
        System.out.println(s);
    }

}
