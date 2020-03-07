package com.example.demo.consumers;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(
        bindings = @QueueBinding(
                value = @Queue("${mq.config.queue.push.name}"),
                exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.FANOUT)
        )
)
public class PushConsumer {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("<<=== push " + msg);
    }

}
