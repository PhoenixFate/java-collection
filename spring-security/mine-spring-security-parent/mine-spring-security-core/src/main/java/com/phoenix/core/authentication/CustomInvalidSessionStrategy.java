package com.phoenix.core.authentication;

import com.phoenix.base.result.RequestResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义session失效策略
 */
@Component
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    /**
     * session 失效后的处理策略，返回json字符串
     * @param request request
     * @param response response
     * @throws IOException IOException
     */
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestResult result = RequestResult.build(HttpStatus.UNAUTHORIZED.value(), "登录已超时，请重新登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result.toJsonString());
    }
}
