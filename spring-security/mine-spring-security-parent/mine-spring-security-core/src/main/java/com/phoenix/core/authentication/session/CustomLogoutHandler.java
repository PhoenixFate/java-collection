package com.phoenix.core.authentication.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出登录处理器，用于解决退出登录后无法再次登录的问题（前提是限制了登录N台电脑）
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/9/2 9:54
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //退出登录，调用自定义退出登录handler，清除缓存中的数据，防止出现N台电脑登录退出后无法再次登录
        //退出登录之后，将对应的session从缓存(SessionRegistryImpl.principals) 中删除
        sessionRegistry.removeSessionInformation(request.getSession().getId());
    }
}
