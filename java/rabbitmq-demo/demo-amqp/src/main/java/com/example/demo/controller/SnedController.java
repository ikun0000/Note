package com.example.demo.controller;

import com.example.demo.config.TTLQueueConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SnedController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/hello/{message}")
    public String sendMessage(@PathVariable("message") String message) {
        if (message.startsWith("xa")) {
            amqpTemplate.convertAndSend(TTLQueueConfig.X,
                    TTLQueueConfig.XA, message.substring(2));
            return "delay 10s";
        } else if (message.startsWith("xb")) {
            amqpTemplate.convertAndSend(TTLQueueConfig.X,
                    TTLQueueConfig.XB, message.substring(2));
            return "delay 40s";
        } else {
            return "error";
        }
    }
}
