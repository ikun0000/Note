package org.example.bridge;

public abstract class Computer {
    protected Brand brand;

    public Computer(Brand brand) {
        this.brand = brand;
    }

    public void info() {
        brand.info();
    }
}
