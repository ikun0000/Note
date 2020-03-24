package org.example.builderpattern.builder2;

public class Chef extends Builder {

    private Product product;

    public Chef() {
        product = new Product();
    }

    @Override
    Builder setBurger(String msg) {
        product.setBurger(msg);
        System.out.println("build burger");
        return this;
    }

    @Override
    Builder setDrink(String msg) {
        product.setDrink(msg);
        System.out.println("build drink");
        return this;
    }

    @Override
    Builder setSnack(String msg) {
        product.setSnack(msg);
        System.out.println("build snack");
        return this;
    }

    @Override
    Builder setDessert(String msg) {
        product.setDessert(msg);
        System.out.println("build dessert");
        return this;
    }

    @Override
    Product getProduct() {
        return product;
    }
}
