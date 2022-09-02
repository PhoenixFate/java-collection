package com.phoenix.core.authentication;

import com.phoenix.base.result.RequestResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.session.InvalidSessionStrategy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义session失效策略
 */
//并不通过@Component注解来注入到容器中，统一通过SecurityConfigBean来管理所有的security相关的bean，实现可自定义弹性配置的注入
//@Component
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    /**
     * session 失效后的处理策略，response返回json字符串 (默认为session失效重定向到登录页面)
     *
     * @param request  request
     * @param response response
     * @throws IOException IOException
     */
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //需要把cookie中的jSessionId删除，要不然访问登录页面等依然会显示：登录已超时，请重新登录
        cancelCookie(request, response);

        RequestResult result = RequestResult.build(HttpStatus.UNAUTHORIZED.value(), "登录已超时，请重新登录");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(result.toJsonString());
    }

    /**
     * 去掉请求中的 JSESSIONID
     *
     * @param request  request
     * @param response response
     */
    private void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }


}
