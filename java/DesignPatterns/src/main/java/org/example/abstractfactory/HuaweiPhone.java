package org.example.abstractfactory;

public class HuaweiPhone implements Phone {
    @Override
    public void call() {
        System.out.println("华为手机打电话");
    }

    @Override
    public void sendSMS() {
        System.out.println("华为手机发短信");
    }

    @Override
    public void playGame() {
        System.out.println("华为手机玩王者荣耀");
    }
}
