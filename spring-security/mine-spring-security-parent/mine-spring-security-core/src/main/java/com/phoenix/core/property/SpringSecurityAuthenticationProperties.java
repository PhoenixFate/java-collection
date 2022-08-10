package com.phoenix.core.property;

import lombok.Data;

@Data
public class SpringSecurityAuthenticationProperties {

    /**
     * mine:
     *   security:
     *     authentication:
     *       loginPage: /login/page # 响应认证（登录）页面的url
     *       loginProcessingUrl: /login/form # 登录表单提交处理的url
     *       usernameParameter: username # 登录表单提交的用户名的属性名
     *       passwordParameter: password # 登录表单提交的密码的属性名
     *       staticPath: # 静态资源路径 /dist/** /modules/** /plugins/**
     *         - /dist/**
     *         - /modules/**
     *         - /plugins/**
     *       loginType: JSON # 认证之后 响应的类型 JSON/REDIRECT
     */

    private String loginPage;

    private String loginProcessingUrl;

    private String usernameParameter;

    private String passwordParameter;

    private String[] staticPath;

    /**
     * 认证响应的类型：JSON REDIRECT
     */
    private String loginReturnType;
}
