package org.example.builderpattern.builder2;

public class TestDemo {

    public static void main(String[] args) {
        Builder builder = new Chef();

        Product product = builder.setBurger("板烧鸡腿堡")
                .setDessert("麦旋风")
                .setDrink("果粒橙")
                .setSnack("薯格")
                .getProduct();

        System.out.println(product);
    }

}
