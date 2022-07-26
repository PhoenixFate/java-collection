package com.phoenix.workflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.workflow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在前后端分离的项目中，使用spring security后在未登录的情况下，请求后台需要认证后才可以访问接口，
 * Spring Security 默认会重定向到 AuthenticationEntryPoint 接口响应302状态
 * 我们重新实现响应JSON，让前端接受处理
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        //50008 未登录
        Result result = Result.build(50008,"请先登录再访问");
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }


}
