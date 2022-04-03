package com.rabbitmq.tutorial.consumer.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ={ "short.message.direct.queue" })
public class DirectShortMessageConsumer {

    @RabbitHandler
    public void receiveMessage(String message){
        System.out.println("short message --- receive the order info -> "+message);
    }
}
