package com.example.demo.consumer;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.info.name}"),
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.TOPIC),
                key = "*.log.info"
        )
)
public class InfoConsumer {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("info receive: " + msg);
    }
}
