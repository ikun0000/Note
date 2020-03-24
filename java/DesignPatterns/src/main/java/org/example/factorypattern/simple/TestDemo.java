package org.example.factorypattern.simple;

public class TestDemo {

    public static void main(String[] args) {
        Car c1 = CarFactory.getCar("大众");
        Car c2 = CarFactory.getCar("特斯拉");
        Car c3 = CarFactory.getDazhong();
        Car c4 = CarFactory.getTesil();

        c1.name();
        c2.name();
        c3.name();
        c4.name();
    }

}
