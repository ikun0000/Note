package com.jvm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        List<String> peoples = Arrays.asList("aaaaa", "bbbbb", "ccccc", "ddddd", "eeeee", "fffff");

//        closeDoor(peoples);

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (String people : peoples) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            }, people).start();
        }
        countDownLatch.await();

        System.out.println(Thread.currentThread().getName() + " finish!");

    }

    public static void closeDoor(List<String> peoples) {
        for (String people : peoples) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
            }, people).start();
        }

        System.out.println(Thread.currentThread().getName() + " finish!");
    }

}
