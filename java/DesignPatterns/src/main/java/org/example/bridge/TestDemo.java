package org.example.bridge;

public class TestDemo {

    public static void main(String[] args) {
        Computer computer1 = new Desktop(new Lenavo());
        Computer computer2 = new Laptop(new Apple());

        computer1.info();
        computer2.info();
    }

}
