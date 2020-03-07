package com.jvm;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("*****" + Thread.currentThread().getName());

        TimeUnit.SECONDS.sleep(4);
        return 1024;
    }
}


public class CallableDemo {

    public static void main(String[] args) throws Exception {

        FutureTask futureTask = new FutureTask(new MyThread());

        new Thread(futureTask, "AAAAA").start();
        new Thread(futureTask, "BBBBB").start();

        System.out.println(futureTask.get());
    }
}
