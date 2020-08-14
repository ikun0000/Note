package org.example.simple;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {

    public static void main(String[] args) throws MQClientException {
        // 实例化消费者，需要指定消费者所属的组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GroupA");
        // 连接RocketMQ的Name Server
        consumer.setNamesrvAddr("10.10.10.246:9876");
        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("TopicA", "TagA||TagB");

        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                synchronized (this) {
                    for (MessageExt msg : msgs) {
                        System.out.printf("Message ID: %s%n", msg.getMsgId());
                        System.out.printf("Body CRC: %d%n", msg.getBodyCRC());
                        byte[] bodyMsg = msg.getBody();
                        System.out.printf("Message: %s%n", new String(bodyMsg, 0, bodyMsg.length));
                        System.out.println("--------------------");
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 开始消费
        consumer.start();
        System.out.println("Consumer Started...");
    }

}
