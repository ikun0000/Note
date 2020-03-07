package com.jvm;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class MyCache {
    private volatile Map<String, Object> map = new HashMap<>();
    public ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    public Lock readLock = readWriteLock.readLock();
    public Lock writeLock = readWriteLock.writeLock();

    public void put(String key, Object value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\twriting!");
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\twrited!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public Object get(String key) {
        readLock.lock();
        Object object = null;
        try {
            System.out.println(Thread.currentThread().getName() + "\treading!");
            object = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\treaded!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return object;
    }
}

public class ReadWirteLockDemo {

    public static void main(String[] args) {
        MyCache myCache = new MyCache();

        for (int i = 0; i < 5; i++) {
            final int count = i;
            new Thread(() -> {
                myCache.put("thread" + count, UUID.randomUUID().toString());
            }, "write thread").start();
        }

        for (int i = 0; i < 5; i++) {
            final int count = i;
            new Thread(() -> {
                String obj = (String) myCache.get("thread" + count);
                System.out.println("key: " + "thread" + count + "\tvalue: " + obj);
            }, "read thread").start();
        }

    }

}
