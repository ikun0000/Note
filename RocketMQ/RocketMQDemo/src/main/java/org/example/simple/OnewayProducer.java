package org.example.simple;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class OnewayProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        // 实例化生产者，需要指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        // 设置Name Server地址
        producer.setNamesrvAddr("10.10.10.246:9876");
        // 启动生产者实例
        producer.start();

        Message message = new Message("TopicA",
                "TagA",
                "One way Producer".getBytes(RemotingHelper.DEFAULT_CHARSET));

        // 使用sendOneWay直接发送而不接收ACK包
        producer.sendOneway(message);

        // 等待关闭退出
        Thread.sleep(5000);
        producer.shutdown();
    }

}
