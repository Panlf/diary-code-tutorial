package com.rabbitmq.tutorial.test;

import com.rabbitmq.tutorial.producer.RabbitMQProducerApplication;
import com.rabbitmq.tutorial.producer.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = RabbitMQProducerApplication.class)
public class RabbitMQTest {
    @Resource
    private OrderService orderService;

    @Test
    void test(){
        orderService.makeOrderDirect("1","1",12);
    }
}
