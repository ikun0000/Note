package com.example.demo.consumer;


import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.info.name}"),
                exchange = @Exchange(value = "${mq.config.exchange}"),
                key = "${mq.config.queue.info.routing.key}"
        )
)
public class InfoConsumer {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("info receive: " + msg);
    }
}
