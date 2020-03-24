package org.example.builderpattern.builder2;

public class Product {
    private String burger;
    private String drink;
    private String snack;
    private String dessert;

    public String getBurger() {
        return burger;
    }

    public void setBurger(String burger) {
        this.burger = burger;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getSnack() {
        return snack;
    }

    public void setSnack(String snack) {
        this.snack = snack;
    }

    public String getDessert() {
        return dessert;
    }

    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    @Override
    public String toString() {
        return "Product{" +
                "burger='" + burger + '\'' +
                ", drink='" + drink + '\'' +
                ", snack='" + snack + '\'' +
                ", dessert='" + dessert + '\'' +
                '}';
    }
}
