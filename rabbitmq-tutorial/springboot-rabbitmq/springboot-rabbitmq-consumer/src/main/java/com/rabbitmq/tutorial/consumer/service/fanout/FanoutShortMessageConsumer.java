package com.rabbitmq.tutorial.consumer.service.fanout;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ={ "short.message.fanout.queue" })
public class FanoutShortMessageConsumer {

    @RabbitHandler
    public void receiveMessage(String message){
        System.out.println("short message --- receive the order info -> "+message);
    }
}
