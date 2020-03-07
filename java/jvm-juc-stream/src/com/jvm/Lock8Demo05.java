package com.jvm;

import java.util.concurrent.TimeUnit;

class Phone {
    public static synchronized void sendEmail() throws Exception {
        TimeUnit.SECONDS.sleep(4);
        System.out.println("*****send Email");
    }

    public synchronized void sendSMS() throws Exception {
        System.out.println("*****send SMS");
    }

    public void sayHello() throws Exception {
        System.out.println("*****hello");
    }
}


public class Lock8Demo05 {

    public static void main(String[] args) {
        Phone phone = new Phone();
        Phone phone1 = new Phone();

        new Thread(() -> {
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();

        new Thread(() -> {
            try {
                phone1.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();


    }

}
