package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列模式
 * 需要打开多个consumer
 * 默认是轮询模式
 *
 */
public class TestProducer {

    private static final Logger LOGGER= LoggerFactory.getLogger(TestProducer.class);

    //队列名
    private static final String QUEUE="hello_world";

    public static void main(String[] args) {
        //通过接连工厂创建新连接和mq建立连接
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("114.67.89.253");
        connectionFactory.setPort(40572);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机, 一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        //建立新的连接
        Connection connection=null;
        Channel channel=null;
        try {
            connection=connectionFactory.newConnection();
            //创建会话通道，生产者和mq服务所有的通信都在channel通道中完成
            channel=connection.createChannel();
            //声明队列, 如果队列在mq中没有则要创建
            //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /*
             * 参数
             * 1. queue 队列名称
             * 2. durable 是否持久化，如果持久化，mq重启后队列还在
             * 3. exclusive 是否独立连接，队列只允许在该连接中访问，如果连接关闭队列自动删除，如果将此参数设置为true可用于临时队列的删除
             * 4. autoDelete 自动删除，队列不再使用的时候是否自动删除，如果将exclusive和autoDelete设置为true，可实现临时队列
             * 5. arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             */
            channel.queueDeclare(QUEUE,true,false,false,null);
            //发送消息
            //String exchange, String routingKey, BasicProperties props, byte[] body
            /*
             * 参数明细
             * 1. exchange 交换机，不指定为默认交换机（设置为空串 ""）
             * 2. routingKey 路由key，交换机根据路由key来将消息转发到指定队列, 如果使用默认交换机，routingKey需要设置为队列的名称
             * 3. props 消息的属性
             * 4. body 消息的内容
             */
            String message="hello world from rabbitmq-producer 中文测试";
            channel.basicPublish("",QUEUE,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("send to mq "+message +" success");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            //关闭通道
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            //关闭连接
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main2(String[] args) {
        String message="hello world from rabbitmq-producer 中文测试";
        LOGGER.info("send to mq {} success",message);
    }



}
