package com.phoenix.workflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.workflow.utils.Result;
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
 * 自定义spring security 认证失败处理器: 响应json结果给前端进行处理，比如：跳转到首页
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, exception);
        response.setContentType("application/json;charset=UTF-8");
        //401 认证失败
        Result result = Result.build(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
    }
}
