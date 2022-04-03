package com.rabbitmq.tutorial.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectRabbitMQConfiguration {

    //1. 声明注册direct模式的交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct_order_exchange",true,false);
    }

    //2. 声明队列
    @Bean
    public Queue smsDirectQueue(){
        return new Queue("sms.direct.queue",true);
    }

    @Bean
    public Queue shortMessageDirectQueue(){
        return new Queue("short.message.direct.queue",true);
    }

    @Bean
    public Queue emailDirectQueue(){
        return new Queue("email.direct.queue",true);
    }

    //3. 完成绑定
    @Bean
    public Binding smsDirectBinding(){
        return BindingBuilder.bind(smsDirectQueue()).to(directExchange()).with("sms");
    }
    @Bean
    public Binding emailDirectBinding(){
        return BindingBuilder.bind(emailDirectQueue()).to(directExchange()).with("email");
    }
    @Bean
    public Binding shortMessageDirectBinding(){
        return BindingBuilder.bind(shortMessageDirectQueue()).to(directExchange()).with("shortMessage");
    }
}
