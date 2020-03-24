package org.example.prototype.shallow;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestDemo {

    public static void main(String[] args) throws CloneNotSupportedException {
        Date createTime = new Date();
        Video v1 = new Video("java", "mp4", createTime);
        Video v2 = (Video) v1.clone();

        System.out.println("v1: " + v1);
        System.out.println("v1 hash: " + v1.hashCode());

        System.out.println("v2: " + v2);
        System.out.println("v2 hash: " + v2.hashCode());

        createTime.setTime(22333342);

        System.out.println("v1: " + v1);
        System.out.println("v1 hash: " + v1.hashCode());

        System.out.println("v2: " + v2);
        System.out.println("v2 hash: " + v2.hashCode());

    }

}
