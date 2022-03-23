package org.example;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQImpl;
import util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WorkerQueueModeDemo {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        System.out.println("worker listening...");
//        channel.basicConsume(QUEUE_NAME, true,
//                (consumerTag, message) -> {
//                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
//                    System.out.println("consumer2: " + msg);
//                },
//                consumerTag -> {
//                    System.out.println("Message Cancel!");
//                });
        channel.basicQos(5);
        channel.confirmSelect();
        channel.basicConsume(QUEUE_NAME, false,
                (consumerTag, message) -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String msg = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println("consumer: " + msg);
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                },
                consumerTag -> {
                    System.out.println("Message Cancel!");
                });

//        DefaultConsumer consumer = new DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//                String msg = new String(body, StandardCharsets.UTF_8);
//                System.out.println("consumer1: " + msg);
//            }
//        };
//        channel.basicConsume(QUEUE_NAME, true, consumer);

//        channel.close();
//        connection.close();
    }
}
