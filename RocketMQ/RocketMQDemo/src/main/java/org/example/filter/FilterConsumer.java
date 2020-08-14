package org.example.filter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class FilterConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("GroupA");
        consumer.setNamesrvAddr("10.10.10.246:9876");
        // 订阅Topic和使用MessageSelector.bySql设置SQL筛选条件
        consumer.subscribe("TopicA", MessageSelector.bySql("a > 300"));
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    synchronized (this) {
                        System.out.printf("Message ID: %s%n", msg.getMsgId());
                        System.out.printf("Message: %s%n", new String(msg.getBody(), 0, msg.getBody().length));
                        System.out.println("--------------------");
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }

}
