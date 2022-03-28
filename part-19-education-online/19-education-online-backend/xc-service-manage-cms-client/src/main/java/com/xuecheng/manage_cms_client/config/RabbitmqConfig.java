package com.xuecheng.manage_cms_client.config;

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

    //队列bean的名称
    public static final String QUEUE_CMS_POST_PAGE = "queue_cms_post_page";
    //交换机的名称
    public static final String EXCHANGE_ROUTING_CMS_POST_PAGE = "exchange_routing_cms_post_page";
    //队列的名称
    @Value("${xuecheng.mq.queue}")
    public String queue_cms_post_page_name;
    //routingKey 即站点Id
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;

    /**
     * 创建ex_routing_cms_postpage 交换机
     * 交换机配置使用direct类型
     *
     * @return the exchange
     */
    @Bean(EXCHANGE_ROUTING_CMS_POST_PAGE)
    public Exchange declareExchange() {
        return ExchangeBuilder.directExchange(EXCHANGE_ROUTING_CMS_POST_PAGE).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_CMS_POST_PAGE)
    public Queue declareQueue() {
        Queue queue = new Queue(queue_cms_post_page_name);
        return queue;
    }

    /**
     * 绑定队列到交换机
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_CMS_POST_PAGE) Queue queue, @Qualifier(EXCHANGE_ROUTING_CMS_POST_PAGE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

}
