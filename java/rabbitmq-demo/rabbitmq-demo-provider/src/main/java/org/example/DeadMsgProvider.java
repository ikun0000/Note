package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class DeadMsgProvider {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Message: ");
            String s = scanner.nextLine();
            if ("quit".equals(s)) {
                break;
            }

            channel.basicPublish(NORMAL_EXCHANGE, "test", null, s.getBytes(StandardCharsets.UTF_8));
        }
    }
}
