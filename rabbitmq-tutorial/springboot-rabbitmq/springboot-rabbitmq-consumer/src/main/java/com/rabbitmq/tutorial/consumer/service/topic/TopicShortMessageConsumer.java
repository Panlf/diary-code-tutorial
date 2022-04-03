package com.rabbitmq.tutorial.consumer.service.topic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "short.message.topic.queue",durable = "true",autoDelete = "false"),
        exchange = @Exchange(value = "topic_order_exchange",type = ExchangeTypes.TOPIC),
        key = "#.shortMessage.#"
))
public class TopicShortMessageConsumer {

    @RabbitHandler
    public void receiveMessage(String message){
        System.out.println("short message --- receive the order info -> "+message);
    }
}
