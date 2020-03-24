package org.example.singletonpattern.ehanshigaijin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

// 实现单利模式
class Product {
    // 定义静态内部类并持有自己的静态对象，并且类加载时就创建，要实现可见性
    private static class Inner {
        public static Product instance = new Product();
    }
    // 私有化构造器
    private Product() {}

    // 提供一个获取本类对象的方法
    public static Product getInstance() {
        return Inner.instance;
    }
}

public class SingletonDemo04 {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Product p1 = Product.getInstance();
        Product p2 = Product.getInstance();

        // 使用反射破坏单例
        Constructor<Product> productConstructor = Product.class.getDeclaredConstructor(null);
        productConstructor.setAccessible(true);
        Product p3 = productConstructor.newInstance(null);


        System.out.println(p1 == p2);
        System.out.println(p1 == p3);
        System.out.println(p2 == p3);
    }

}
