package org.example.factorypattern.method;

public class TesilFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new TeslCar();
    }
}
