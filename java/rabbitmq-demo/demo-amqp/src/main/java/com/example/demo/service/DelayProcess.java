package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DelayProcess {

    private static final Logger logger = LoggerFactory.getLogger("DelayProcess");

    @RabbitListener(queues = "QD")
    public void delayMessage(String message) {
        logger.info(message);
    }

}
