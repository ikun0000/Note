package org.example.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

public class TransactionListenerImpl implements TransactionListener {

    // 这个方法就是执行本地事务的
    // 这个方法会在调用sendMessageInTransaction之后执行
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        if (arg == null) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    // 这个方法是反查本地事务状态，根据本地事务状态提交或回滚事务消息
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
