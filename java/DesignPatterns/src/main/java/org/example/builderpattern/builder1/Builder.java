package org.example.builderpattern.builder1;

public abstract class Builder {

    abstract void setBurger();
    abstract void setDrink();
    abstract void setSnack();
    abstract void setDessert();

    abstract Product getProduct();

}
