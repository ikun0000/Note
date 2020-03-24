package org.example.prototype.deep;

import java.util.Date;

public class TestDemo {

    public static void main(String[] args) throws CloneNotSupportedException {
        Date createTime = new Date();
        Video v1 = new Video("python", "mp4", createTime);
        Video v2 = (Video) v1.clone();

        System.out.println("v1: " + v1);
        System.out.println("v2: " + v2);

        createTime.setTime(2333333);

        System.out.println("v1: " + v1);
        System.out.println("v2: " + v2);

    }

}
