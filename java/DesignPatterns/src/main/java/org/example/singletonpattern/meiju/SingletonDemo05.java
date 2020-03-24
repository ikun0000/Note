package org.example.singletonpattern.meiju;

enum Product {
    INSTANCE;

    Product() {
    }
}

public class SingletonDemo05 {

    public static void main(String[] args) {
        Product p1 = Product.INSTANCE;
        Product p2 = Product.INSTANCE;

        System.out.println(p1 == p2);
    }

}
