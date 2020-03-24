package org.example.factorypattern.method;

public class TestDemo {

    public static void main(String[] args) {
        Car c1 = new TesilFactory().getCar();
        Car c2 = new DazhongFactory().getCar();

        c1.name();
        c2.name();
    }

}
