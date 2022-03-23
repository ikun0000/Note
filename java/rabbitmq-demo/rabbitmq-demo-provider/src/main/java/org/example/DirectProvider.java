package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DirectProvider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("my_direct", BuiltinExchangeType.DIRECT);
        channel.queueDeclare("my_direct_queue1", true, false, false, null);
        channel.queueDeclare("my_direct_queue2", true, false, false, null);
        channel.queueBind("my_direct_queue1", "my_direct", "11111");
        channel.queueBind("my_direct_queue2", "my_direct", "22222");

        channel.basicPublish("my_direct", "11111", null, "aaaaa".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("my_direct", "22222", null, "bbbbb".getBytes(StandardCharsets.UTF_8));
    }
}
