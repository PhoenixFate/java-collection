package com.xuecheng.manage_media_process.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:04
 **/
@Configuration
public class RabbitMQConfig {

    public static final String EX_MEDIA_PROCESS_TASK = "ex_media_processor";

    //视频处理队列
    @Value("${xc-service-manage-media.mq.queue-media-video-processor}")
    public String queue_media_video_process_task;

    //视频处理路由
    @Value("${xc-service-manage-media.mq.routing-key-media-video}")
    public String routing_key_media_video;

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;


    /**
     * 交换机配置
     *
     * @return the exchange
     */
    @Bean(EX_MEDIA_PROCESS_TASK)
    public Exchange EX_MEDIA_VIDEO_TASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESS_TASK).durable(true).build();
    }

    //声明队列
    @Bean("queue_media_video_process_task")
    public Queue QUEUE_PROCESS_TASK() {
        Queue queue = new Queue(queue_media_video_process_task, true, false, true);
        return queue;
    }

    /**
     * 绑定队列到交换机 .
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_media_process_task(@Qualifier("queue_media_video_process_task") Queue queue, @Qualifier(EX_MEDIA_PROCESS_TASK) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routing_key_media_video).noargs();
    }
}
