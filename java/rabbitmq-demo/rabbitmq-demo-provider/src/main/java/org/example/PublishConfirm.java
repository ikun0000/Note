package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import util.RabbitMQUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class PublishConfirm {

    private static final int COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {

//        publishMessageIndividually();

//        publishMessageBatch();

        publishMessageAsync();
    }

    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("PC1", false, false, false, null);
        channel.confirmSelect();
        for (int i = 0; i < COUNT; i++) {
            channel.basicPublish("", "PC1", null,
                    String.format("message%d", i).getBytes(StandardCharsets.UTF_8));
            if (channel.waitForConfirms()) {
                System.out.println("success send");
            } else {
                System.out.println("fail send");
            }
        }
    }

    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("PC1", false, false, false, null);
        int confirmCount = 200;
        channel.confirmSelect();
        for (int i = 0; i < COUNT; i++) {
            channel.basicPublish("", "PC1",  null,
                    String.format("message%d", i).getBytes(StandardCharsets.UTF_8));

            if (--confirmCount == 0) {
                if (channel.waitForConfirms()) {
                    System.out.println("success confirm");
                } else {
                    System.out.println("fail confirm");
                }
                confirmCount = 200;
            }
        }
    }

    public static void publishMessageAsync() throws IOException, TimeoutException {
        Connection connection = RabbitMQUtil.getConnection();
        Channel channel = connection.createChannel();

        ConcurrentSkipListMap<Long, String> container = new ConcurrentSkipListMap<>();

        channel.queueDeclare("PC1", false, false, false, null);
        channel.confirmSelect();
        channel.addConfirmListener((deliveryTag, multiple) -> {
            System.out.println("ack " + deliveryTag);
            container.remove(deliveryTag);
        }, (deliveryTag, multiple) -> {
            System.out.println("nack " + deliveryTag);
        });

        for (int i = 0; i < COUNT; i++) {
            String msg = String.format("message %d", i);
            container.put(channel.getNextPublishSeqNo(), msg);
            channel.basicPublish("", "PC1", null,
                    msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
