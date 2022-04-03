package com.rabbitmq.tutorial.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static Runnable runnable =()->{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置连接属性
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //获取队列名称
        final String queueName = Thread.currentThread().getName();

        Connection connection = null;
        Channel channel = null;
        try {
            //从连接工厂中获取连接
            connection = connectionFactory.newConnection("fanout-consumer");

            //从连接中获取通道Channel
            channel = connection.createChannel();

            Channel finalChannel = channel;

            finalChannel.basicConsume(queueName, true, (s, delivery) -> {
                System.out.println(delivery.getEnvelope().getDeliveryTag());
                System.out.println(queueName+": receive message :"+new String(delivery.getBody(),"UTF-8"));

                //如果改成false，则是手动应答
                //finalChannel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }, (s) -> {

            });
            System.out.println(queueName + ":"+ "receive message");
            System.in.read();
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
    };


    public static void main(String[] args) {
        //启动三个线程执行
        new Thread(runnable,"fanout-queue1").start();
        new Thread(runnable,"fanout-queue2").start();
        new Thread(runnable,"fanout-queue3").start();
    }

}
