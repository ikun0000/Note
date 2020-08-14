package org.example.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class OrderedConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GroupA");
        consumer.setNamesrvAddr("10.10.10.246:9876");

        /**
         * 设置Consumer第一次启动是从队   列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TopicA", "TagA");

        // 使用MessageListenerOrderly的实现去处理每条队列的顺序消息
        consumer.registerMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                context.setAutoCommit(true);
                for (MessageExt msg : msgs) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(msg.getBody());
                    try {
                        // 反序列化传输的对象并打印Queue ID和对象内容
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        OrderedProducer.OrderStep orderStep = (OrderedProducer.OrderStep) ois.readObject();
                        synchronized (this) {
                            System.out.printf("Queue ID: %d%n", msg.getQueueId());
                            System.out.printf("Payload: %s%n", orderStep);
                            System.out.println("--------------------");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Consumer Started...");

    }

}
