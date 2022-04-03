package com.rabbitmq.tutorial.producer.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class OrderService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void makeOrder(String userId,String productId,int num){
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生成成功："+orderId);

        String exchangeName = "fanout_order_exchange";
        String routingKey="";
        //交换机 路由key/queue队列名称 消息内容
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId);
    }

    public void makeOrderDirect(String userId,String productId,int num){
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生成成功："+orderId);

        String exchangeName = "direct_order_exchange";
        String routingKey="sms";
        //交换机 路由key/queue队列名称 消息内容
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId);
    }
    public void makeOrderTopic(String userId,String productId,int num){
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生成成功："+orderId);

        String exchangeName = "topic_order_exchange";
        String routingKey="";
        //交换机 路由key/queue队列名称 消息内容
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId);
    }

    public void makeOrderTTLMessage(String userId,String productId,int num){
        String orderId = UUID.randomUUID().toString();
        System.out.println("订单生成成功："+orderId);

        String exchangeName = "ttl_direct_exchange";
        String routingKey="ttl_message";

        //给消息设置过期时间
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("5000");
                message.getMessageProperties().setContentEncoding("UTF-8");
                return message;
            }
        };

        //交换机 路由key/queue队列名称 消息内容
        rabbitTemplate.convertAndSend(exchangeName,routingKey,orderId,messagePostProcessor);
    }

}
