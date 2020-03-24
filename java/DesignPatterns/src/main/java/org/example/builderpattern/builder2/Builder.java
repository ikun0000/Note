package org.example.builderpattern.builder2;

public abstract class Builder {

    abstract Builder setBurger(String msg);
    abstract Builder setDrink(String msg);
    abstract Builder setSnack(String msg);
    abstract Builder setDessert(String msg);

    abstract Product getProduct();

}
