package com.rabbitmq.tutorial.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接属性
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        Connection connection = null;
        Channel channel = null;
        try {
            //从连接工厂中获取连接
            connection = connectionFactory.newConnection("direct-producer");

            //从连接中获取通道Channel
            channel = connection.createChannel();

            //发送消息
            String message = "direct-producer send data hello world";

            //准备交换机
            String exchangeName = "direct-exchange";

            //定义路由key
            String routingKey = "boy";

            //指定交换机的类型　
            // 交换机类型 direct/topic/fanout/headers
            String exchangeType = "direct";

            //声明交换机
            //true 是否持久化
            channel.exchangeDeclare(exchangeName,exchangeType,true);

            //声明队列
            //1 队列的名称

            //2 是否持久化, 队列的声明默认是存放到内存中的，
            // 如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，保存到Erlang自带的Mnesia数据库中，
            // 当rabbitmq重启之后会读取该数据库

            //3 设置是否排他, 为 true 则设置队列为排他的。如果一个队列被声明为排他队列，
            // 该队列仅对首次声明它的连接可见，并在连接断开时自动删除。
            // 这里需要注意三点:排他队列是基于连接(Connection) 可见的，
            // 同个连接的不同信道 (Channel) 是可以同时访问同一连接创建的排他队列; "首次"是指如果这个连接己经声明了排他队列，
            // 其他连接是不允许建立同名的排他队列的，这个与普通队列不同:即使该队列是持久化的，一旦连接关闭或者客户端退出，
            // 该排他队列都会被自动删除，这种队列 适用于一个客户端同时发送和读取消息的应用场景。

            //4 设置是否自动删除。为true则设置队列为自动删除。
            // 自动删除的前提是: 至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，才会自动删除。
            // 不能把这个参数错误地理解为: "当连接到此队列的所有客户端断开时，这个队列自动删除"，
            // 因为生产者客户端创建这个队列，或者没有消费者客户端与这个队 列连接时，都不会自动删除这个队列。

            //5 设置队列的其他一些参数，如 x-rnessage-ttl 、x-expires 、x-rnax-length 、
            // x-rnax-length-bytes、 x-dead-letter-exchange、 x-deadletter-routing-key 、
            // x-rnax-priority 等。

            channel.queueDeclare("direct-queue1",true,false,false,null);
            channel.queueDeclare("direct-queue2",true,false,false,null);
            channel.queueDeclare("direct-queue3",true,false,false,null);

            //声明队列和交换机的关系
            channel.queueBind("direct-queue1",exchangeName,"boy");
            channel.queueBind("direct-queue2",exchangeName,"boy");
            channel.queueBind("direct-queue3",exchangeName,"girl");


            //发送消息给中间件rabbitmq-server
            channel.basicPublish(exchangeName,routingKey, null,message.getBytes());

        }catch (Exception e){

        }finally {
            if(connection!=null && connection.isOpen()){
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(channel!=null && channel.isOpen()){
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
