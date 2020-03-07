package com.jvm;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotSafeDemo01 {

    public static void main(String[] args) {
        List<String> stringList = new CopyOnWriteArrayList<>();

        for (int i = 0; i <= 30; i++) {
            new Thread(() -> {
                stringList.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(stringList);
            }, "thread-" + i).start();
            
        }
        
    }

}
