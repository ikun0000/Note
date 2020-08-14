package org.example.filter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class FilterProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        producer.setNamesrvAddr("10.10.10.246:9876");
        producer.start();

        Message message1 = new Message("TopicA",
                "TagA",
                "Filter Message 1".getBytes(RemotingHelper.DEFAULT_CHARSET));
        // 给消息添加用户自定义属性
        message1.putUserProperty("a", "500");
        producer.send(message1);

        Message message2 = new Message("TopicA",
                "TagA",
                "Filter Message 2".getBytes(RemotingHelper.DEFAULT_CHARSET));
        message2.putUserProperty("a", "200");
        producer.send(message2);

        producer.shutdown();
    }

}
