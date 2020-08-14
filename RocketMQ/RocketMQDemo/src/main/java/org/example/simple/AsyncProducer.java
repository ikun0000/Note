package org.example.simple;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.TimeUnit;

public class AsyncProducer {

    public static void main(String[] args) throws Exception {
        // 实例化生产者，需要指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 设置Name Server地址
        producer.setNamesrvAddr("10.10.10.246:9876");
        // 启动生产者实例
        producer.start();
        // 设置消息重投
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        // 实例化一个计数器
        CountDownLatch2 countDownLatch2 = new CountDownLatch2(messageCount);

        for (int i = 0; i < messageCount; i++) {
            // 封装消息
            Message message = new Message("TopicA",
                    "TagA",
                    "OrderID1",     // 业务标识的Key
                    ("Async Message" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));

            // 发送消息，并且传入SendCallback的实现异步处理返回的ACK
            producer.send(message, new SendCallback() {
                // 成功发送并接收到ACK
                @Override
                public void onSuccess(SendResult sendResult) {
                    show(sendResult);
                    countDownLatch2.countDown();
                }

                // 发送失败
                @Override
                public void onException(Throwable e) {
                    System.err.println("Exception: " + e);
                    countDownLatch2.countDown();
                }
            });
        }

        // 等待消息响应后关闭生产者实例退出程序
        countDownLatch2.await(5, TimeUnit.SECONDS);
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
