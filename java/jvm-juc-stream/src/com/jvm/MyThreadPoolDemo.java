package com.jvm;


import java.util.concurrent.*;

public class MyThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();
        ExecutorService threadPool3 = Executors.newCachedThreadPool();
        ExecutorService threadPool4 = new ThreadPoolExecutor(2,
                Runtime.getRuntime().availableProcessors() + 1,
                10L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(4),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        try {
            for (int i = 0; i < 20; i++) {
//                threadPool3.execute(() -> {
//                    System.out.println(Thread.currentThread().getName() + " come in");
//                });

                threadPool4.submit(() -> {
                    System.out.println(Thread.currentThread().getName() + " come in");
                    try { TimeUnit.MICROSECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
                });

//                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            threadPool4.shutdown();
        }

    }

}
