package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        System.out.println("my_topic consumer1");
        channel.basicConsume("my_topic_queue1", true, (consumerTag, message) -> {
                    System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
                },
                consumerTag -> {});
    }
}
