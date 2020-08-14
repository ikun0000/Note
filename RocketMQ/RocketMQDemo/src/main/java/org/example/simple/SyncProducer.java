package org.example.simple;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class SyncProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        // 实例化生产者，需要指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        // 设置Name Server地址
        producer.setNamesrvAddr("10.10.10.246:9876");
        // 启动生产者实例
        producer.start();

        // 实例化消息
        Message message1 = new Message("TopicA",    // 目标Topic
                "TagA",                              // 消息的Tag
                "First Message".getBytes(RemotingHelper.DEFAULT_CHARSET));  // 消息体
        Message message2 = new Message("TopicA",
                "TagB",
                "Second Message".getBytes(RemotingHelper.DEFAULT_CHARSET));

        SendResult sendResult1 = producer.send(message1);      // 发送消息
        SendResult sendResult2 = producer.send(message2);

        // 打印SendResult内容
        show(sendResult1);
        show(sendResult2);

        // 关闭生产者实例
        producer.shutdown();
    }

    // 显示SendResult
    private static void show(SendResult sendResult) {
        System.out.printf("Message ID: %s%n", sendResult.getMsgId());
        System.out.printf("Transaction ID: %s%n", sendResult.getTransactionId());
        System.out.printf("Region ID: %s%n", sendResult.getRegionId());
        System.out.printf("Status: %s%n", sendResult.getSendStatus());
        System.out.println("--------------------");
    }

}
