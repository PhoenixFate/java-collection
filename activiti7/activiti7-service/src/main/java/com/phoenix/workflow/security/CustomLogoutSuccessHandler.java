package com.phoenix.workflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.workflow.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出成功，处理器 handler
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/7/26 17:24
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        Result result = Result.ok("退出成功！");
        String json = objectMapper.writeValueAsString(result);
        httpServletResponse.getWriter().write(json);
    }

}
