package com.rabbitmq.tutorial.topic;

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
            connection = connectionFactory.newConnection("topic-producer");

            //从连接中获取通道Channel
            channel = connection.createChannel();

            //发送消息
            String message = "topic-producer send data hello world";

            //准备交换机
            String exchangeName = "topic-exchange";

            //定义路由key
            String routingKey = "boy.children";

            //指定交换机的类型　
            // 交换机类型 direct/topic/fanout/headers
            String exchangeType = "topic";

            //声明交换机
            //true 是否持久化
            channel.exchangeDeclare(exchangeName,exchangeType,true);

            //声明队列
            channel.queueDeclare("topic-queue1",true,false,false,null);
            channel.queueDeclare("topic-queue2",true,false,false,null);
            channel.queueDeclare("topic-queue3",true,false,false,null);

            //声明队列和交换机的关系
            //* (星号) 可以代替一个完整的单词.至少1个
            //# (井号) 可以代替零个或多个单词.0 1 多个
            channel.queueBind("topic-queue1",exchangeName,"boy.#");
            channel.queueBind("topic-queue2",exchangeName,"girl.#");
            channel.queueBind("topic-queue3",exchangeName,"#.adult.#");


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
