package com.phoenix.core.authentication.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码异常类
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/11 17:33
 */
public class ValidationImageCodeException extends AuthenticationException {

    public ValidationImageCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidationImageCodeException(String msg) {
        super(msg);
    }
}
