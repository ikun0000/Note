package org.example.abstractfactory;

public class TestDemo {

    public static void main(String[] args) {
        System.out.println("==================小米系列产品==================");
        ProductFactory xiaomiProductFactory = new XiaomiProductFactory();
        Phone p1 = xiaomiProductFactory.getPhone();
        Router r1 = xiaomiProductFactory.getRouter();

        p1.call();
        p1.sendSMS();
        p1.playGame();

        r1.openWifi();
        r1.setting();

        System.out.println("==================华为系列产品==================");
        ProductFactory huaweiProductFactory = new HuaweiProductFactory();
        Phone p2 = huaweiProductFactory.getPhone();
        Router r2 = huaweiProductFactory.getRouter();

        p2.call();
        p2.sendSMS();
        p2.playGame();

        r2.openWifi();
        r2.setting();
    }

}
