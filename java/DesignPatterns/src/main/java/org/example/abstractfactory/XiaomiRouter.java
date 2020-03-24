package org.example.abstractfactory;

public class XiaomiRouter implements Router {
    @Override
    public void openWifi() {
        System.out.println("小米路由器开启WIFI");
    }

    @Override
    public void setting() {
        System.out.println("小米路由器设置");
    }
}
