package com.example.demo.consumer;


import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue(value = "${mq.config.queue.error.name}"),
                exchange = @Exchange(value = "${mq.config.exchange}"),
                key = "${mq.config.queue.error.routing.key}"
        )
)
public class ErrorConsumer {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("error receive: " + msg);
    }
}
