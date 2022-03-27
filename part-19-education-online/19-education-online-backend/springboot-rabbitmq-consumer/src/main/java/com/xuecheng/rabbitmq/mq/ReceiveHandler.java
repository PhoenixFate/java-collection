package com.xuecheng.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {

    /**
     * 监听消息队列
     */
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void sendEmail(String message, Message message2, Channel channel){
        System.out.println("接受到的消息: "+message);
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void sendSms(String message, Message message2, Channel channel){
        System.out.println("接受到的消息: "+message);
    }

}
