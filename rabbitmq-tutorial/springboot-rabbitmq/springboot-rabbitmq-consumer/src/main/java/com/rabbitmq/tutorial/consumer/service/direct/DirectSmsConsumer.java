package com.rabbitmq.tutorial.consumer.service.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ={ "sms.direct.queue" })
public class DirectSmsConsumer {
    @RabbitHandler
    public void receiveMessage(String message){
        System.out.println("sms --- receive the order info -> "+message);
    }
}
