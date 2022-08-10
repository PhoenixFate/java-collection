package com.phoenix.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/10 15:25
 */
@Getter
@AllArgsConstructor
public enum LoginResponseType {

    /**
     * 响应JSON字符串
     */
    JSON(1,"JSON"),

    /**
     * 重定向地址
     */
    REDIRECT(2,"REDIRECT");

    private final Integer code;

    private final String type;

}
