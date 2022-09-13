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
    /**
     * 登录表单提交处理的url
     */
    private String loginProcessingUrl;
    /**
     * 退出登录表单处理url
     */
    private String logoutProcessingUrl;
    /**
     * 退出登录成功后的跳转地址
     */
    private String logoutSuccessUrl;

    private String usernameParameter;

    private String passwordParameter;

    private String[] staticPath;

    private String[] deleteCookies;

    /**
     * 认证响应的类型：JSON REDIRECT
     */
    private String loginReturnType;
    /**
     * /code/image # 获取图形验证码地址
     */
    private String imageCodeUrl;
    /**
     * /code/mobile #发送手机验证码地址
     */
    private String mobileCodeUrl;
    /**
     * /mobile/page # 前往手机登录页面
     */
    private String mobilePage;
    /**
     * 记住我功能有效时长
     * 60*60*24*7=604800  7天
     */
    private Integer tokenValiditySeconds;
}
