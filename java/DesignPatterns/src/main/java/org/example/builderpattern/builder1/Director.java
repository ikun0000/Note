package org.example.builderpattern.builder1;

public class Director {

    public Product build(Builder builder) {
        builder.setBurger();
        builder.setDessert();
        builder.setSnack();
        builder.setDrink();

        return builder.getProduct();
    }

}
