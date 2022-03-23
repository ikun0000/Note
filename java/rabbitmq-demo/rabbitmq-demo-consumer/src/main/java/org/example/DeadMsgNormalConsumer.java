package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DeadMsgNormalConsumer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String NORMAL_QUEUE = "normal_queue";
    private static final String DEAD_EXCHANGE = "dead_queue";
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT, true);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT, true);

        Map<String, Object> argument = new HashMap<>();

//        argument.put("x-message-ttl", Integer.valueOf(5000));
//        argument.put("x-max-length", Integer.valueOf(10));
        argument.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        argument.put("x-dead-letter-routing-key", "later");
        channel.queueDeclare(NORMAL_QUEUE, true, false, false, argument);
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);

        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "test");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "later");

        System.out.println("normal ready");
        channel.basicConsume(NORMAL_QUEUE, false,
                (consumerTag, message) -> {
                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    if (msg.startsWith("inject")) {
                        System.out.println("reject message");
//                        channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                        channel.basicNack(message.getEnvelope().getDeliveryTag(), false, false);
                    } else {
                        System.out.println("normal: " + msg);
                        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                    }
                }, consumerTag -> {});
    }
}
