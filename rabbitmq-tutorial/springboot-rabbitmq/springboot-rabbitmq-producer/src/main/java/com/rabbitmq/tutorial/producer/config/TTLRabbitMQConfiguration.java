package com.rabbitmq.tutorial.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TTLRabbitMQConfiguration {

    @Bean
    public DirectExchange ttlDirectExchange(){
        return new DirectExchange("ttl_direct_exchange",true,false);
    }

    @Bean
    public Queue directTTLQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",5000); //这里一定是int类型
        args.put("x-dead-letter-exchange","dead_direct_exchange");
        args.put("x-dead-letter-routing-key","dead");//fanout 不需要配置
        return new Queue("ttl.direct.queue",true,false,false,args);
    }
    @Bean
    public Queue directTTLMessageQueue(){
        return new Queue("ttl.message.direct.queue",true);
    }

    @Bean
    public Binding directSmsTTLBinding(){
        return BindingBuilder.bind(directTTLQueue()).to(ttlDirectExchange()).with("ttl");
    }
    @Bean
    public Binding directSmsTTLMessageBinding(){
        return BindingBuilder.bind(directTTLMessageQueue()).to(ttlDirectExchange()).with("ttl_message");
    }

}
