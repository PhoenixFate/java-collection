package com.phoenix.oauth2.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 加载中文的认证提示信息配置
 *
 * @Author phoenix
 * @Date 10/22/22 22:04
 * @Version 1.0
 */
@Configuration
public class ReloadMessageConfig {

    /**
     * 加载中文的认证提示信息
     *
     * @return ReloadableResourceBundleMessageSource
     */
    @Bean
    public ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        //原文件的位置：classpath:org/springframework/security/message_zh_CN，拷贝到自己的classpath下， 可以进行修改
        //reloadableResourceBundleMessageSource.setBasename("classpath:org/springframework/security/message_zh_CN");//不需要后缀.properties
        reloadableResourceBundleMessageSource.setBasename("classpath:message_zh_CN");//不需要后缀.properties
        return reloadableResourceBundleMessageSource;
    }


}
