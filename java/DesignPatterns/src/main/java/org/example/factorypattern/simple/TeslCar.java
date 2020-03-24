package org.example.factorypattern.simple;

public class TeslCar implements Car {
    @Override
    public void name() {
        System.out.println("特斯拉汽车");
    }
}
