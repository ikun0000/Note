package org.example.batch;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SplitProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        producer.setNamesrvAddr("10.10.10.246:9876");
        producer.start();

        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message("TopicA", "Batch message 1".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messageList.add(new Message("TopicA", "Batch message 2".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messageList.add(new Message("TopicA", "Batch message 3".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        ListSplitter listSplitter = new ListSplitter(messageList);

        while (listSplitter.hasNext()) {
            producer.send(listSplitter.next());
        }

        producer.shutdown();
    }

    private static class ListSplitter implements Iterator<List<Message>> {
        private static final int SIZE_LIMIT = 1024 * 1024 * 4;
        private final List<Message> messageList;
        private int currentIndex = 0;

        private ListSplitter(List<Message> messageList) {
            this.messageList = messageList;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < messageList.size();
        }

        @Override
        public List<Message> next() {
            int startIndex = getStartIndex();
            int nextIndex = startIndex;
            int totalSize = 0;

            // 循环计算开始索引和结束索引
            for (; nextIndex < messageList.size(); nextIndex++) {
                Message message = messageList.get(nextIndex);
                int tmpSize = calcMessageSize(message);
                // 如果之前的Message大小加上当前Message的大小超过限制则停止
                if (tmpSize + totalSize > SIZE_LIMIT) {
                    break;
                } else {
                    totalSize += tmpSize;
                }
            }

            List<Message> subList = messageList.subList(startIndex, nextIndex);
            currentIndex = nextIndex;
            return subList;
        }

        // 防止单个Message大于4MB，如果一个Message大于4MB则直接跳过不发送
        private int getStartIndex() {
            Message currentMessage = messageList.get(currentIndex);
            int tmpSize = calcMessageSize(currentMessage);
            while (tmpSize > SIZE_LIMIT) {
                currentIndex++;
                Message message = messageList.get(currentIndex);
                tmpSize = calcMessageSize(message);
            }

            return currentIndex;
        }

        // 计算Message的大小
        private int calcMessageSize(Message message) {
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            tmpSize = tmpSize + 20; // 增加⽇日志的开销20字节
            return tmpSize;
        }
    }

}
