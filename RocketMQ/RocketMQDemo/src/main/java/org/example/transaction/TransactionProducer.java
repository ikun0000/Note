package org.example.transaction;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.*;

public class TransactionProducer {

    public static void main(String[] args) throws Exception {
        // 初始化事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("GroupA");
        producer.setNamesrvAddr("10.10.10.246:9876");
        // 给事务生产者提供的线程池
        ExecutorService executorService = new ThreadPoolExecutor(2,
                5,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("client-transaction-msg-check-thread");
                        return thread;
                    }
                });
        producer.setExecutorService(executorService);

        // 初始化该事务消息对应的本地事务和本地事务反查对象
        TransactionListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionListener(transactionListener);

        producer.start();

        Message successMessage = new Message("TopicA",  "TagA", "Success".getBytes(RemotingHelper.DEFAULT_CHARSET));
        Message failureMessage = new Message("TopicA", "TagA", "Failure".getBytes(RemotingHelper.DEFAULT_CHARSET));

        // 把消息发送到事务里
        SendResult sendResult1 = producer.sendMessageInTransaction(successMessage, Integer.parseInt("1"));
        SendResult sendResult2 = producer.sendMessageInTransaction(failureMessage, null);

        System.out.printf("Success Message: %s%n", sendResult1.getSendStatus());
        System.out.printf("Failure Message: %s%n", sendResult2.getSendStatus());

        producer.shutdown();
    }

}
