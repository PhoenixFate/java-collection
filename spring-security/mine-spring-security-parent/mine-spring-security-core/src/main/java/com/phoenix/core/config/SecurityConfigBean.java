package com.phoenix.core.config;

import com.phoenix.core.authentication.CustomAuthenticationFailureHandler;
import com.phoenix.core.authentication.mobile.SmsCodeSender;
import com.phoenix.core.authentication.mobile.SmsSend;
import com.phoenix.core.authentication.session.CustomInvalidSessionStrategy;
import com.phoenix.core.authentication.session.CustomSessionInformationExpiredStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 主要为容器中添加bean实例
 *
 * @author phoenix
 * @version 1.0.0
 * @date 2022/8/12 16:20
 */
@Configuration
public class SecurityConfigBean {

    /**
     * “@ConditionalOnMissingBean(SmsSend.class)”:
     * 默认情况下，采用的是SmsCodeSender实例，
     * 但是如果容器中有其他SmsSend类型的实例，
     * 那么当前的这个SmsCodeSender就失效的了
     *
     * @return SmsCodeSender
     */
    @Bean
    @ConditionalOnMissingBean(SmsSend.class)
    public SmsSend smsSend() {
        return new SmsCodeSender();
    }

    /**
     * spring security默认为SessionRegistryImpl
     * 直接注入，spring security已经创建好了
     */
    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * session失效后的处理类
     *
     * @return InvalidSessionStrategy
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy InvalidSessionStrategy() {
        return new CustomInvalidSessionStrategy(sessionRegistry);
    }

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new CustomSessionInformationExpiredStrategy(customAuthenticationFailureHandler);
    }

}
