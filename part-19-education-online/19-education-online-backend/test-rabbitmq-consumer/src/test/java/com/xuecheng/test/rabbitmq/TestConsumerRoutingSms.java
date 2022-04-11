package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 路由模式
 *  消费者
 */
public class TestConsumerRoutingSms {

    //队列名
    private static final String QUEUE_INFORM_SMS="queue_inform_sms";
    //交换机名称
    private static final String EXCHANGE_ROUTING_INFORM="exchange_routing_inform";
    //routingKey 路由key
    private static final String ROUTING_KEY_SMS="inform_sms";

    public static void main(String[] args) {
        //通过接连工厂创建新连接和mq建立连接
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("www.bytes-space.com");
        connectionFactory.setPort(40572);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机, 一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");
        //建立新的连接
        Connection connection=null;
        try {
            connection=connectionFactory.newConnection();
            //创建会话通道，生产者和mq服务所有的通信都在channel通道中完成
            Channel channel=connection.createChannel();
            //监听队列
            //声明队列, 如果队列在mq中没有则要创建 （主要防止消费者先启动，生产者未启动未声明队列而报错）
            //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /*
             * 参数
             * 1. queue 队列名称
             * 2. durable 是否持久化，如果持久化，mq重启后队列还在
             * 3. exclusive 是否独立连接，队列只允许在该连接中访问，如果连接关闭队列自动删除，如果将此参数设置为true可用于临时队列的删除
             * 4. autoDelete 自动删除，队列不再使用的时候是否自动删除，如果将exclusive和autoDelete设置为true，可实现临时队列
             * 5. arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             */
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
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);

            //交换机和队列 进行绑定
            /*
                String queue, String exchange, String routingKey
                queue 队列名称
                exchange 交换机名称
                routingKey 路由key，作用是交换机根据路由key的值将消息转发到指定的队列中，但发布订阅模式中设置为空串
             */
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,ROUTING_KEY_SMS);

            //实现消费方法
            DefaultConsumer defaultConsumer=new DefaultConsumer(channel){
                //当接受到消息后此方法将被调用
                /*
                    consumerTag 消费者标签，用来标识消费者的，在监听队列时设置channel.basicConsume
                    envelope 信封
                    properties
                    body
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //交换机
                    String exchange = envelope.getExchange();
                    //消息id, mq在通道中用来标识消息的id，可用于消息已经接收
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息的属性：properties
                    //消息的内容 body
                    String message=new String(body, StandardCharsets.UTF_8);
                    System.out.println("receive a message "+message);
                    super.handleDelivery(consumerTag, envelope, properties, body);
                }
            };

            //监听队列
            //参数：String queue boolean autoAck Consumer callback
            /*
             * 参数明细
             * queue 队列名称
             * autoAck 自动回复，当消费者接收到消息后告诉mq消息已经接受，如果将此参数设置为true表示自动回复mq，如果设置为false，则通过编程回复
             * callback 消费方法，当消费者接受到消息执行的方法
             */
            channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
