package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicProvider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("my_topic", BuiltinExchangeType.TOPIC);
        channel.queueDeclare("my_topic_queue1", true, false, false, null);
        channel.queueDeclare("my_topic_queue2", true, false, false, null);
        channel.queueBind("my_topic_queue1", "my_topic", "test.*.log");
        channel.queueBind("my_topic_queue2", "my_topic", "test.*.warn");

        channel.basicPublish("my_topic", "test.aaa.log", null, "aaaaa".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("my_topic", "test.bbbb.log", null, "bbbbb".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("my_topic", "test.aaa.warn", null, "ccccc".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("my_topic", "test.bbbbb.warn", null, "ddddd".getBytes(StandardCharsets.UTF_8));
    }
}
