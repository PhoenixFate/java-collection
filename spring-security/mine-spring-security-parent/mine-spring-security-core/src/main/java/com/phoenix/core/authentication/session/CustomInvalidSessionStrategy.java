package com.phoenix.core.authentication.session;

import com.phoenix.base.result.RequestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
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
@Slf4j
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    /**
     * session注册机制，spring security默认为SessionRegistryImpl()
     */
    private final SessionRegistry sessionRegistry;

    public CustomInvalidSessionStrategy(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * session 失效后的处理策略，response返回json字符串 (默认为session失效重定向到登录页面)
     *
     * @param request  request
     * @param response response
     * @throws IOException IOException
     */
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //session过期之后调用该方法，所以request.getSession()无法获得以前的session，会重新创建一个session，所以request.getSession().getId()不对
        log.info("--------------- getSession().getId(): {}", request.getSession().getId());
        //request.getRequestedSessionId()从当前请求的cookie中获取sessionId
        log.info("--------------- getRequestedSessionId(): {}", request.getRequestedSessionId());
        //通过sessionRegistry，删除spring security缓存中失效的session 用于解决设置允许最大登录次数和满了最大登录次数之后不允许再登录，然后session超时失效，无法再次登录的问题
        sessionRegistry.removeSessionInformation(request.getRequestedSessionId());

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
