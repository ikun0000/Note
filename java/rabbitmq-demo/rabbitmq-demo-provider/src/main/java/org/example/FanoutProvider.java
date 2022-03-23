package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class FanoutProvider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("my_fanout", BuiltinExchangeType.FANOUT,
                true);
        channel.queueDeclare("my_fanout_queue1", true, false, false, null);
        channel.queueDeclare("my_fanout_queue2", true, false, false, null);
        channel.queueBind("my_fanout_queue1", "my_fanout", "");
        channel.queueBind("my_fanout_queue2", "my_fanout", "");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Message: ");
            String s = scanner.nextLine();
            if ("quit".equals(s)) {
                break;
            }

            channel.basicPublish("my_fanout", "", null, s.getBytes(StandardCharsets.UTF_8));
        }


    }
}
