package com.phoenix.core.config;

import com.phoenix.core.mobile.SmsCodeSender;
import com.phoenix.core.mobile.SmsSend;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * @return SmsCodeSender
     */
    @Bean
    @ConditionalOnMissingBean(SmsSend.class)
    public SmsSend smsSend() {
        return new SmsCodeSender();
    }

}
