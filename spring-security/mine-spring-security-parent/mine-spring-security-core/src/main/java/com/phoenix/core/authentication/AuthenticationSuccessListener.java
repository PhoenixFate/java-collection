package com.phoenix.core.authentication;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用来监听认证成功之后的处理器
 * 也就是认证成功之让认证成功处理器调用此接口的方法
 *
 * @Author phoenix
 * @Date 2022/9/27 10:31
 * @Version 1.0.0
 */
public interface AuthenticationSuccessListener {

    void successListener(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

}
