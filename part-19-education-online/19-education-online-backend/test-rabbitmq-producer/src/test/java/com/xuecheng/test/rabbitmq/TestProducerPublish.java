package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式
 *
 */
public class TestProducerPublish {

    private static final Logger LOGGER= LoggerFactory.getLogger(TestProducerPublish.class);

    //队列名
    private static final String QUEUE_INFORM_EMAIL="queue_inform_email";
    private static final String QUEUE_INFORM_SMS="queue_inform_sms";
    //交换机名称
    private static final String EXCHANGE_FANOUT_INFORM="exchange_fanout_inform";


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
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            //声明一个交换机
            //String exchange, String type
            /*
               交换机名称 exchange
               交换机类型 type
                    fanout: 对应的rabbitmq的工作模式是 publish/subscribe 发布订阅模式
                    direct: 对应Routing 路由工作模式
                    topic:  对应Topics 通配符的工作模式
                    headers: 对应headers 工作模式
             */
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

            //交换机和队列 进行绑定
            /*
                String queue, String exchange, String routingKey
                queue 队列名称
                exchange 交换机名称
                routingKey 路由key，作用是交换机根据路由key的值将消息转发到指定的队列中，但发布订阅模式中设置为空串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_FANOUT_INFORM,"");
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_FANOUT_INFORM,"");

            //发送消息
            //String exchange, String routingKey, BasicProperties props, byte[] body
            /*
             * 参数明细
             * 1. exchange 交换机，不指定为默认交换机（设置为空串 ""）
             * 2. routingKey 路由key，交换机根据路由key来将消息转发到指定队列, 如果使用默认交换机，routingKey需要设置为队列的名称
             * 3. props 消息的属性
             * 4. body 消息的内容
             */
            for(int i=0;i<5;i++){
                String message="send inform message to user "+i;
                channel.basicPublish(EXCHANGE_FANOUT_INFORM,"",null,message.getBytes(StandardCharsets.UTF_8));
                System.out.println("send to mq "+message +" success");
            }
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
