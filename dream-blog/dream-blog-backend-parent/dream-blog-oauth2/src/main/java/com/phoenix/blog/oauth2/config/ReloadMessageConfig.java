package com.phoenix.blog.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 加载多语言的认证提示信息配置
 *
 * @Author phoenix
 * @Date 10/22/22 22:04
 * @Version 1.0
 */
@Configuration
public class ReloadMessageConfig {

    /**
     * 加载中文的认证提示信息
     * Spring Security里面的异常本身已经是做了多语言的，但是只支持通过Accept-Language来切换错误信息的语言
     * @return ReloadableResourceBundleMessageSource
     */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        // 原文件的位置：classpath:org/springframework/security/messages_zh_CN，拷贝到自己的classpath下， 可以进行修改
        // reloadableResourceBundleMessageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");//不需要后缀.properties
        // reloadableResourceBundleMessageSource.setBasename("classpath:messages_zh_CN");//不需要后缀.properties
        reloadableResourceBundleMessageSource.setBasename("classpath:i18n/messages");//不需要后缀.properties
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return reloadableResourceBundleMessageSource;
    }


}
