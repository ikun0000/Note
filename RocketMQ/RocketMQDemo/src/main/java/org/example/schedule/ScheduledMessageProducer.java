package org.example.schedule;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class ScheduledMessageProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        producer.setNamesrvAddr("10.10.10.246:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message message = new Message("TopicA",
                    ("Scheduled message" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            /**
             * 设置消息的延时级别，RocketMQ不支持自定义延时时间，只能选择延时级别
             * 这条消息会在10s后才会被消费
             *
             * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
             * 对应的延时级别为1~18
             */
            message.setDelayTimeLevel(3);
            producer.send(message);
        }

        producer.shutdown();
    }

}
