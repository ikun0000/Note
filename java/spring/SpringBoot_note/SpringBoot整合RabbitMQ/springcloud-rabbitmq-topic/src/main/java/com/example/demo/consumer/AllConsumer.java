package com.example.demo.consumer;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue("${mq.config.queue.all.name}"),
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.TOPIC),
                key = "*.log.*"
        )
)
public class AllConsumer {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("all receive: " + msg);
    }

}
