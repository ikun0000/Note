package com.example.demo.provider;


import com.example.demo.enums.RoutingKeys;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Provider {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${mq.config.exchange}")
    private String exchange;

    public void send(RoutingKeys routingKeys, String msg) {
        amqpTemplate.convertAndSend(exchange, routingKeys.getRoutingKey(), msg);
    }

}
