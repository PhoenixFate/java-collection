package com.xuecheng.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    //队列名
    public static final String QUEUE_INFORM_EMAIL="queue_inform_email_spring";
    public static final String QUEUE_INFORM_SMS="queue_inform_sms_spring";
    //交换机名称
    public static final String EXCHANGE_TOPIC_INFORM="exchange_topic_inform_spring";

    //routingKey 路由key
    //通配符： .# 表示任意0个或者1个或者多个单词
    //       .* 表示任意0个或者1个单词
    public static final String ROUTING_KEY_EMAIL="inform.#.email.#"; //可以匹配inform.email/inform.email.sms/inform.sms.email 不能匹配inform.sms
    public static final String ROUTING_KEY_SMS="inform.#.sms.#";

    //声明交换机
    @Bean(EXCHANGE_TOPIC_INFORM)
    public Exchange declareExchange(){
        //durable(true): 表示持久化，mq重启之后，交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_INFORM).durable(true).build();
    }

    //声明队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue declareQueueEmail(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    //声明队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue declareQueueSms(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //邮箱队列绑定交换机
    @Bean
    public Binding bindingQueueAndExchangeEmail(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,@Qualifier(EXCHANGE_TOPIC_INFORM)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_EMAIL).noargs();
    }

    //短信队列绑定交换机
    @Bean
    public Binding bindingQueueAndExchangeSms(@Qualifier(QUEUE_INFORM_SMS) Queue queue,@Qualifier(EXCHANGE_TOPIC_INFORM)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SMS).noargs();
    }

}
