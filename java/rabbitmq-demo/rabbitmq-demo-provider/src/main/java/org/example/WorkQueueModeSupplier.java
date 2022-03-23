package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class WorkQueueModeSupplier {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Please enteru message: ");
            String s = scanner.nextLine();
            if ("quit".equals(s)) {
                break;
            }
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    s.getBytes(StandardCharsets.UTF_8));
        }

        channel.close();
        connection.close();
    }
}
