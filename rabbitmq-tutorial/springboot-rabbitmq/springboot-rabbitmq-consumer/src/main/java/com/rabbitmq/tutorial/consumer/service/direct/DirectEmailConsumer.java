package com.rabbitmq.tutorial.consumer.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ={ "email.direct.queue" })
public class DirectEmailConsumer {
    @RabbitHandler
    public void receiveMessage(String message){
        System.out.println("email --- receive the order info -> "+message);
    }
}
