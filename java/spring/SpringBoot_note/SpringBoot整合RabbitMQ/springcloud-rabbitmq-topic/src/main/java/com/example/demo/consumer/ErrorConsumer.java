package com.example.demo.consumer;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.error.name}"),
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.TOPIC),
                key = "*.log.error"
        )
)
public class ErrorConsumer {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("error receive: " + msg);
    }
}
