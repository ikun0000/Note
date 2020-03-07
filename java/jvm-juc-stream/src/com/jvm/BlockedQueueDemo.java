package com.jvm;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockedQueueDemo {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

//        System.out.println(blockingQueue.add("a"));
//        System.out.println(blockingQueue.add("b"));
//        System.out.println(blockingQueue.add("c"));
//        System.out.println(blockingQueue.remove());
//        System.out.println(blockingQueue.add("d"));
//        System.out.println(blockingQueue.element());

//        System.out.println(blockingQueue.offer("a"));
//        System.out.println(blockingQueue.offer("b"));
//        System.out.println(blockingQueue.offer("c"));
//        System.out.println(blockingQueue.poll());
//        System.out.println(blockingQueue.offer("d"));
//        System.out.println(blockingQueue.peek());

//        blockingQueue.put("a");
//        blockingQueue.put("b");
//        blockingQueue.put("c");
//        System.out.println(blockingQueue.take());
//        blockingQueue.put("d");
//        System.out.println(blockingQueue.take());
//        blockingQueue.put("f");
//        blockingQueue.put("d");
//        blockingQueue.put("g");

        blockingQueue.offer("a", 1, TimeUnit.SECONDS);
        blockingQueue.offer("a", 1, TimeUnit.SECONDS);
        blockingQueue.offer("a", 1, TimeUnit.SECONDS);
        blockingQueue.offer("a", 1, TimeUnit.SECONDS);

        blockingQueue.poll(1, TimeUnit.SECONDS);
        blockingQueue.poll(1, TimeUnit.SECONDS);
        blockingQueue.poll(1, TimeUnit.SECONDS);
        blockingQueue.poll(1, TimeUnit.SECONDS);
        blockingQueue.poll(1, TimeUnit.SECONDS);
    }

}
