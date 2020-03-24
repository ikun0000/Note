package org.example.singletonpattern.ehanshi;

// 实现单利模式
class Product {
    // 持有自己的静态对象，并且类加载时就创建
    private static Product instance = new Product();

    // 私有化构造器
    private Product() {}

    // 提供一个获取本类对象的方法
    public static Product getInstance() {
        return instance;
    }
}

public class SingletonDemo01 {

    public static void main(String[] args) {
        Product p1 = Product.getInstance();
        Product p2 = Product.getInstance();

        System.out.println(p1 == p2);
    }

}
