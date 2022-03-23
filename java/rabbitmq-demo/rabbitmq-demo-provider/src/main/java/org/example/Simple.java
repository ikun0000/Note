package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Simple {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false,
                false, false, null);

        channel.basicPublish("", QUEUE_NAME, null,
                "Hello World!".getBytes(StandardCharsets.UTF_8));

        System.out.println("message sended");

        channel.close();
        connection.close();
    }
}
