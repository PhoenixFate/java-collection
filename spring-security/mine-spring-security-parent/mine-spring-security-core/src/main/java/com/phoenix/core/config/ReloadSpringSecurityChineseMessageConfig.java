package com.phoenix.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * spring security 加载中文认证信息的配置类
 */
@Configuration
public class ReloadSpringSecurityChineseMessageConfig {

    /**
     * 加载中文的spring security认证提示信息
     * @return ReloadableResourceBundleMessageSource
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource=new ReloadableResourceBundleMessageSource();
        //PROPERTIES_SUFFIX = ".properties"; 源码中会自动添加后缀，所以不需要添加后缀
        ///Users/phoenix/.m2/repository/org/springframework/security/spring-security-core/5.2.0.RELEASE/spring-security-core-5.1.7.RELEASE.jar!/org/springframework/security/messages_zh_CN.properties
        messageSource.setBasename("classpath:org/springframework/security/messages_zh_CN");
        return messageSource;
    }

}
