package org.example.factorypattern.method;

public class TeslCar implements Car {
    @Override
    public void name() {
        System.out.println("特斯拉汽车");
    }
}
