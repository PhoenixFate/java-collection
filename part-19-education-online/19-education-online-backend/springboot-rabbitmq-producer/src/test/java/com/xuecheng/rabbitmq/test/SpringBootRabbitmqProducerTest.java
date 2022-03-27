package com.xuecheng.rabbitmq.test;

import com.xuecheng.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootRabbitmqProducerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用rabbitTemplate 向email发送消息
     */
    @Test
    public void testSendEmail(){
        //String exchange, String routingKey, Object message
        /*
            exchange 交换机的名称
            routingKey
            message 消息内容
         */
        String message="我来自spring boot";
        System.out.println(rabbitTemplate);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC_INFORM,"inform.email",message);

    }



}
