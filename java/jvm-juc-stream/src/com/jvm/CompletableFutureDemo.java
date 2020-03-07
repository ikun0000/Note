package com.jvm;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " no return!!");
        });

        voidCompletableFuture.whenComplete((t, u) -> {
            System.out.println("t: " + t);
            System.out.println("u: " + u);
        }).get();

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
//            int a = 10 / 0;
            return UUID.randomUUID().toString().replaceAll("-", "");
        });

        String result = stringCompletableFuture.whenComplete((arg1, arg2) -> {
            System.out.println("res: " + arg1);
            System.out.println("u: " + arg2);
        }).exceptionally(ex -> {
            return ex.getMessage();
        }).get();

        System.out.println(result);
    }

}
