package com.jvm;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ShareData {
    private int number = 1;
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    private void printN(int n, String context, int who,  Condition wait, int nwho, Condition signal) {
        lock.lock();
        try {
            while (number != who) {
                wait.await();
            }

            for (int i = 0; i < n; i++) {
                System.out.println(context);
            }

            number = nwho;
            signal.signal();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void print5() { printN(5, Thread.currentThread().getName(), 1, c1, 2, c2); }
    public void print10() { printN(10, Thread.currentThread().getName(), 2, c2, 3, c3); }
    public void print15() { printN(15, Thread.currentThread().getName(), 3, c3, 1, c1); }

}

public class ConditionDemo {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print5();
            }
        }, "AAAAA").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print10();
            }
        }, "BBBBB").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                shareData.print15();
            }
        }, "CCCCC").start();


    }

}
