package com.jvm;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AirCondition {
    private int number = 0;

//    public synchronized void inc() throws Exception {
//        // 判断
//        while (number != 0) {
//            this.wait();
//        }
//
//        // 生产
//        number++;
//        System.out.println(Thread.currentThread().getName() + " provided! " + number);
//
//        // 通知
//        this.notifyAll();
//
//    }
//
//    public synchronized void dec() throws Exception {
//        // 判断
//        while (number == 0) {
//            this.wait();
//        }
//
//        // 消费
//        number--;
//        System.out.println(Thread.currentThread().getName() + " consumed! " + number);
//
//        // 通知
//        this.notifyAll();
//    }

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void inc() throws Exception {

        lock.lock();
        try {
            while (number != 0) {
                condition.await();
            }

            // 生产
            number++;
            System.out.println(Thread.currentThread().getName() + " provided! " + number);

            // 通知
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void dec() throws Exception {
        lock.lock();
        try {
            // 判断
            while (number == 0) {
                condition.await();
            }

            // 消费
            number--;
            System.out.println(Thread.currentThread().getName() + " consumed! " + number);

            // 通知
            condition.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

public class ProdConsumerDemo04 {

    public static void main(String[] args) throws Exception {
        AirCondition airCondition = new AirCondition();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    airCondition.inc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    airCondition.dec();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    airCondition.inc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    airCondition.dec();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }

}
