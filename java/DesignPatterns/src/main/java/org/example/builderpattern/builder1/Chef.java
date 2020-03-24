package org.example.builderpattern.builder1;

public class Chef extends Builder {

    private Product product;

    public Chef() {
        product = new Product();
    }

    @Override
    void setBurger() {
        product.setBurger("香辣鸡腿堡");
        System.out.println("build burger");
    }

    @Override
    void setDrink() {
        product.setDrink("中可");
        System.out.println("build drink");
    }

    @Override
    void setSnack() {
        product.setSnack("中薯");
        System.out.println("build snack");
    }

    @Override
    void setDessert() {
        product.setDessert("草莓圣代");
        System.out.println("build dessert");
    }

    @Override
    Product getProduct() {
        return product;
    }
}
