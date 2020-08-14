package org.example.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderedProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GroupA");
        producer.setNamesrvAddr("10.10.10.246:9876");
        producer.start();

        List<OrderStep> orderStepList = buildOrders();

        for (OrderStep orderStep : orderStepList) {
            // 直接把对象序列化发送到消息队列中
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(orderStep);
            Message message = new Message("TopicA",
                    "TagA",
                    "OrderId1",
                    baos.toByteArray());

            /**
             * 该send方法第一个参数为发送的消息，第二个参数是MessageQueueSelector的实现
             * 用来告诉程序如何选择消息队列，第三个参数会传递给MessageQueueSelector的select
             * 方法的第二个参数
             */
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Long id = (Long) arg;
                    long index = id % mqs.size();
                    return mqs.get((int) index);
                }
            }, orderStep.getOrderId());

            show(sendResult);
        }

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

    // 产生订单用例
    private static List<OrderStep> buildOrders() {
        List<OrderStep> orderStepList = new ArrayList<>();

        orderStepList.add(new OrderStep(1539L, "create"));
        orderStepList.add(new OrderStep(1565L, "create"));
        orderStepList.add(new OrderStep(1539L, "payment"));
        orderStepList.add(new OrderStep(1535L, "create"));
        orderStepList.add(new OrderStep(1565L, "payment"));
        orderStepList.add(new OrderStep(1535L, "payment"));
        orderStepList.add(new OrderStep(1565L, "complete"));
        orderStepList.add(new OrderStep(1539L, "push"));
        orderStepList.add(new OrderStep(1535L, "complete"));
        orderStepList.add(new OrderStep(1539L, "complete"));
        return orderStepList;
    }

    // 订单实体类
    public static class OrderStep implements Serializable {
        private long orderId;
        private String desc;

        public OrderStep(long orderId, String desc) {
            this.orderId = orderId;
            this.desc = desc;
        }

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return "OrderStep{" +
                    "orderId=" + orderId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

}
