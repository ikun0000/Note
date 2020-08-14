package org.example.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.List;

public class NoSplitProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        producer.setNamesrvAddr("10.10.10.246:9876");
        producer.start();

        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message("TopicA", "Batch message 1".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messageList.add(new Message("TopicA", "Batch message 2".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messageList.add(new Message("TopicA", "Batch message 3".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        producer.send(messageList);

        producer.shutdown();
    }

}
