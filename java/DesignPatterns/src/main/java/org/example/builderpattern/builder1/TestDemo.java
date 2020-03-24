package org.example.builderpattern.builder1;

public class TestDemo {

    public static void main(String[] args) {
        Product product = new Director().build(new Chef());

        System.out.println(product);
    }

}
