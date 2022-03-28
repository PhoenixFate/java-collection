package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
public class RabbitmqConfig {

    //交换机的名称
    public static final String EXCHANGE_ROUTING_CMS_POST_PAGE = "exchange_routing_cms_post_page";

    /**
     * 创建ex_routing_cms_postpage 交换机
     * 交换机配置使用direct类型 (路由类型)
     *
     * @return the exchange
     */
    @Bean(EXCHANGE_ROUTING_CMS_POST_PAGE)
    public Exchange declareExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_ROUTING_CMS_POST_PAGE).durable(true).build();
    }

}
