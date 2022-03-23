package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DeadMsgDeadConsumer {
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        System.out.println("dead ready");
        channel.basicConsume(DEAD_QUEUE, true,
                (consumerTag, message) -> {
                    System.out.println("dead: " + new String(message.getBody(), StandardCharsets.UTF_8));
                }, consumerTag -> {});
    }
}
