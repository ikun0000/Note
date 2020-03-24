package org.example.factorypattern.method;

public class DazhongFactory implements CarFactory {
    @Override
    public Car getCar() {
        return new DazhongCar();
    }
}
